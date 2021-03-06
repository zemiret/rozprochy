package main

import (
	"context"
	"errors"
	"google.golang.org/grpc"
	"log"
	"math/rand"
	"net"
	"os"
	"os/signal"
	pb "server/gen"
	"sync"
	"time"
)

const (
	port = ":20200"
)

var (
	ErrChannelClosed = errors.New("channel has been closed")
)

type observerManager struct {
	ch []chan *pb.Event
	sync.RWMutex
}

var (
	observable observerManager
)

type server struct {
	pb.UnimplementedNotifierServer
}

func shouldReport(req *pb.ObserveRequest, ev *pb.Event) bool {
	return (req.Kingdom == "" || req.Kingdom == ev.Kingdom) &&
		req.Type == ev.Type &&
		req.Severity == ev.Severity
}

func (s *server) Observe(req *pb.ObserveRequest, stream pb.Notifier_ObserveServer) error {
	eventChannel := make(chan *pb.Event)
	observable.Lock()
	observable.ch = append(observable.ch, eventChannel)
	observable.Unlock()

	for {
		ev, more := <-eventChannel
		if more {
			if shouldReport(req, ev) {
				if err := stream.Send(ev); err != nil {
					close(eventChannel)
					observable.Lock()
					var idx int
					for i, v := range observable.ch {
						if v == eventChannel {
							idx = i
							break
						}
					}
					observable.ch = append(observable.ch[:idx], observable.ch[idx+1:]...)
					observable.Unlock()

					return err
				}
			}
		} else {
			log.Println("Channel closed...")
			return ErrChannelClosed
		}
	}
}

func (s *server) Ping(ctx context.Context, empty *pb.Empty) (*pb.Empty, error) {
	return &pb.Empty{}, nil
}

func main() {
	rand.Seed(time.Now().Unix())
	gen := NewGenerator(3)

	lis, err := net.Listen("tcp", port)
	log.Printf("Listening on %s...", port)
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}
	s := grpc.NewServer()
	pb.RegisterNotifierServer(s, &server{})

	go func() {
		for {
			time.Sleep(2 * time.Second)

			ev, err := gen.generateRandomEvent()
			if err != nil {
				log.Println("Error generating event: ", err)
			}

			log.Println("Generated event: ", ev.Description)

			observable.Lock()
			for i := 0; i < len(observable.ch); i++ {
				observable.ch[i] <- ev
			}
			observable.Unlock()
		}
	}()

	c := make(chan os.Signal, 1)
	signal.Notify(c, os.Interrupt)
	signal.Notify(c, os.Kill)
	go func() {
		<-c
		log.Println("Exit handler called...")

		observable.Lock()
		for i := 0; i < len(observable.ch); i++ {
			close(observable.ch[i])
		}
		observable.Unlock()

		os.Exit(0)
	}()

	log.Println("Server started...")
	if err := s.Serve(lis); err != nil {
		log.Fatalf("failed to serve %v", err)
	}
}
