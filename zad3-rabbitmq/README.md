# Building

You need to include the libraries present in the lib folder.
The easiest way to do that is by using IntelIJ.

# Terminology

The admin and agency are self-explanatory.

* **Provider** is the entity that provides services.
* **Capability** is either person, cargo, or satellite - it's what the given provider can handle

# Running

Make sure you have rabbitMQ running, the easiest way to achieve that:
```$xslt
docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management  
```

Module `src.main` contains programs to run.

## Provider

Provider will ask for its name, and the names of 2 capabilities it should handle.

## Agency

Agency will ask for its name, and then it can start dispatching messages.
You just type one of: person, cargo, or satellite, and it will dispatch the job
to the first available provider.

## Admin

To send message from admin, you type them in the following format:
```
toWhom: Message goes here
```

where `toWhom` is one of: all, provider, agency