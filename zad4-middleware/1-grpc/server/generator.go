package main

import (
	"errors"
	"fmt"
	"math/rand"
	pb "server/gen"
)

var (
	ErrKingdomsOverflowed = errors.New("not enough kingdoms")
)

var (
	kingdomNames = []string{
		"Asturia",
		"Liberon",
		"RWP utta",
		"Nakrim",
		"Nashkel",
	}
	armyTypes = []string{
		"orc guerillas",
		"scouts",
		"modern warfare",
	}
	kingNames = []string{
		"Maciuś",
		"Arthur",
		"Pendred",
		"Torcal",
		"Thristen",
		"Izgrael",
		"Sammidth",
		"Cripst",
	}
	cityNames = []string{
		"Beregost",
		"Drumium",
		"Chist",
		"Dropperkritz",
		"Al'drin",
	}
)

type generator struct {
	atMostAffectedKingdoms int
}

func NewGenerator(atMostAffectedKingdoms int) *generator {
	return &generator{
		atMostAffectedKingdoms: atMostAffectedKingdoms,
	}
}

func (g *generator) generateRandomKing(kingdom string) string {
	return fmt.Sprintf("%s of %s", randomValFromStringSlice(kingNames), kingdom)
}

func (g *generator) generateRandomKingdom() string {
	return randomValFromStringSlice(kingdomNames)
}

func (g *generator) generateRandomArmy(kingdom string) string {
	return fmt.Sprintf("%s's %s",
		kingdom, randomValFromStringSlice(armyTypes),
	)
}

func (g *generator) generateRandomCity(kingdom string) string {
	return fmt.Sprintf("%s in %s", randomValFromStringSlice(cityNames), kingdom)
}

func (g *generator) generateAffectedKingdoms(kingdom string) ([]string, error) {
	if g.atMostAffectedKingdoms >= (len(kingdomNames) - 1) {
		return nil, ErrKingdomsOverflowed
	}

	var affected []string

	for i := 0; i < g.atMostAffectedKingdoms; i++ {
		chosen := kingdomNames[rand.Intn(len(armyTypes))]
		if !inStringSlice(chosen, affected) && chosen != kingdom {
			affected = append(affected, chosen)
		}
	}

	return affected, nil
}

func (g *generator) generateKingName() string {
	return kingNames[rand.Intn(len(kingNames))]
}

func (g *generator) generateArmyDefeated() (*pb.Event, error) {
	kingdom := g.generateRandomKingdom()
	affected, err := g.generateAffectedKingdoms(kingdom)
	if err != nil {
		return nil, err
	}
	armyName := g.generateRandomArmy(kingdom)

	return &pb.Event{
		Type:             pb.EventType_ARMY_DEFEATED,
		Severity:         pb.EventSeverity(randomEnumValFromMap(pb.EventSeverity_value)),
		Kingdom:          kingdom,
		AffectedKingdoms: affected,
		Description:      fmt.Sprintf("Army %s has been defeated. Praize their courage!", armyName),
	}, nil
}

func (g *generator) generateCityDestroyed() (*pb.Event, error) {
	kingdom := g.generateRandomKingdom()
	affected, err := g.generateAffectedKingdoms(kingdom)
	if err != nil {
		return nil, err
	}
	city := g.generateRandomCity(kingdom)

	return &pb.Event{
		Type:             pb.EventType_CITY_DESTROYED,
		Severity:         pb.EventSeverity(randomEnumValFromMap(pb.EventSeverity_value)),
		Kingdom:          kingdom,
		AffectedKingdoms: affected,
		Description:      fmt.Sprintf("City %s has been destroyed. No mercy for the victims was shown.", city),
	}, nil
}

func (g *generator) generateKingBanned() (*pb.Event, error) {
	kingdom := g.generateRandomKingdom()
	affected, err := g.generateAffectedKingdoms(kingdom)
	if err != nil {
		return nil, err
	}
	king := g.generateRandomKing(kingdom)

	return &pb.Event{
		Type:             pb.EventType_KING_CHANGED,
		Severity:         pb.EventSeverity(randomEnumValFromMap(pb.EventSeverity_value)),
		Kingdom:          kingdom,
		AffectedKingdoms: affected,
		Description:      fmt.Sprintf("King %s has finished his rule. Long live the new one!", king),
	}, nil
}

func (g *generator) generateRandomEvent() (*pb.Event, error) {
	switch rand.Intn(3) {
	case 0:
		return g.generateArmyDefeated()
	case 1:
		return g.generateCityDestroyed()
	case 2:
		return g.generateKingBanned()
	default:
		return g.generateArmyDefeated()
	}
}
