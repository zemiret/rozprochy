package grpc.types;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.27.0)",
    comments = "Source: types.proto")
public final class NotifierGrpc {

  private NotifierGrpc() {}

  public static final String SERVICE_NAME = "types.Notifier";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<grpc.types.ObserveRequest,
      grpc.types.Event> getObserveMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Observe",
      requestType = grpc.types.ObserveRequest.class,
      responseType = grpc.types.Event.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<grpc.types.ObserveRequest,
      grpc.types.Event> getObserveMethod() {
    io.grpc.MethodDescriptor<grpc.types.ObserveRequest, grpc.types.Event> getObserveMethod;
    if ((getObserveMethod = NotifierGrpc.getObserveMethod) == null) {
      synchronized (NotifierGrpc.class) {
        if ((getObserveMethod = NotifierGrpc.getObserveMethod) == null) {
          NotifierGrpc.getObserveMethod = getObserveMethod =
              io.grpc.MethodDescriptor.<grpc.types.ObserveRequest, grpc.types.Event>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Observe"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.types.ObserveRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.types.Event.getDefaultInstance()))
              .setSchemaDescriptor(new NotifierMethodDescriptorSupplier("Observe"))
              .build();
        }
      }
    }
    return getObserveMethod;
  }

  private static volatile io.grpc.MethodDescriptor<grpc.types.Empty,
      grpc.types.Empty> getPingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Ping",
      requestType = grpc.types.Empty.class,
      responseType = grpc.types.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpc.types.Empty,
      grpc.types.Empty> getPingMethod() {
    io.grpc.MethodDescriptor<grpc.types.Empty, grpc.types.Empty> getPingMethod;
    if ((getPingMethod = NotifierGrpc.getPingMethod) == null) {
      synchronized (NotifierGrpc.class) {
        if ((getPingMethod = NotifierGrpc.getPingMethod) == null) {
          NotifierGrpc.getPingMethod = getPingMethod =
              io.grpc.MethodDescriptor.<grpc.types.Empty, grpc.types.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Ping"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.types.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.types.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new NotifierMethodDescriptorSupplier("Ping"))
              .build();
        }
      }
    }
    return getPingMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static NotifierStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NotifierStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NotifierStub>() {
        @java.lang.Override
        public NotifierStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NotifierStub(channel, callOptions);
        }
      };
    return NotifierStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static NotifierBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NotifierBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NotifierBlockingStub>() {
        @java.lang.Override
        public NotifierBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NotifierBlockingStub(channel, callOptions);
        }
      };
    return NotifierBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static NotifierFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NotifierFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NotifierFutureStub>() {
        @java.lang.Override
        public NotifierFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NotifierFutureStub(channel, callOptions);
        }
      };
    return NotifierFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class NotifierImplBase implements io.grpc.BindableService {

    /**
     */
    public void observe(grpc.types.ObserveRequest request,
        io.grpc.stub.StreamObserver<grpc.types.Event> responseObserver) {
      asyncUnimplementedUnaryCall(getObserveMethod(), responseObserver);
    }

    /**
     */
    public void ping(grpc.types.Empty request,
        io.grpc.stub.StreamObserver<grpc.types.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getPingMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getObserveMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                grpc.types.ObserveRequest,
                grpc.types.Event>(
                  this, METHODID_OBSERVE)))
          .addMethod(
            getPingMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                grpc.types.Empty,
                grpc.types.Empty>(
                  this, METHODID_PING)))
          .build();
    }
  }

  /**
   */
  public static final class NotifierStub extends io.grpc.stub.AbstractAsyncStub<NotifierStub> {
    private NotifierStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NotifierStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NotifierStub(channel, callOptions);
    }

    /**
     */
    public void observe(grpc.types.ObserveRequest request,
        io.grpc.stub.StreamObserver<grpc.types.Event> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getObserveMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void ping(grpc.types.Empty request,
        io.grpc.stub.StreamObserver<grpc.types.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class NotifierBlockingStub extends io.grpc.stub.AbstractBlockingStub<NotifierBlockingStub> {
    private NotifierBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NotifierBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NotifierBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<grpc.types.Event> observe(
        grpc.types.ObserveRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getObserveMethod(), getCallOptions(), request);
    }

    /**
     */
    public grpc.types.Empty ping(grpc.types.Empty request) {
      return blockingUnaryCall(
          getChannel(), getPingMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class NotifierFutureStub extends io.grpc.stub.AbstractFutureStub<NotifierFutureStub> {
    private NotifierFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NotifierFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NotifierFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<grpc.types.Empty> ping(
        grpc.types.Empty request) {
      return futureUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_OBSERVE = 0;
  private static final int METHODID_PING = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final NotifierImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(NotifierImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_OBSERVE:
          serviceImpl.observe((grpc.types.ObserveRequest) request,
              (io.grpc.stub.StreamObserver<grpc.types.Event>) responseObserver);
          break;
        case METHODID_PING:
          serviceImpl.ping((grpc.types.Empty) request,
              (io.grpc.stub.StreamObserver<grpc.types.Empty>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class NotifierBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    NotifierBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return grpc.types.TypesProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Notifier");
    }
  }

  private static final class NotifierFileDescriptorSupplier
      extends NotifierBaseDescriptorSupplier {
    NotifierFileDescriptorSupplier() {}
  }

  private static final class NotifierMethodDescriptorSupplier
      extends NotifierBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    NotifierMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (NotifierGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new NotifierFileDescriptorSupplier())
              .addMethod(getObserveMethod())
              .addMethod(getPingMethod())
              .build();
        }
      }
    }
    return result;
  }
}
