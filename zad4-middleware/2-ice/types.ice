["java:package:server.src.main.java"]
module Smarthouse {
	enum State { Running, Off }

	struct OvenProgram {
		float temperature;
		int hours;
		int minutes;
		int seconds;
	}

	exception GenericError {
		string reason;
	}

	interface Device {
		idempotent string name();
		idempotent State getState();
		void turnOn() throws GenericError;
		void turnOff() throws GenericError;
	}

	interface Oven extends Device {
		idempotent OvenProgram getProgram();
		void setProgram(OvenProgram program) throws GenericError;
	}

	sequence<string> Lines;

	interface Bulbulator extends Device {
		idempotent Lines getJoke();
	}

	interface Wihajster extends Bulbulator {
		void setTarget(string target);
		idempotent string describeTarget();
	}

	interface Mykens extends Bulbulator {
		void setSpeed(int speed);
		idempotent string mykensuj();
	}
}
