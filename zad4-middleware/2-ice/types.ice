["java:package:types"]
module Smarthouse {
	enum State { Running, Off }

	sequence<string> Lines;

	struct OvenProgram {
		float temperature;
		int hours;
		int minutes;
		int seconds;
	}

	exception GenericError {
		string reason;
	}

	interface DeviceCloud {
		idempotent Lines listDevices();
	}

	interface Device {
		idempotent string getName();
		idempotent State getState();
		void turnOn() throws GenericError;
		void turnOff() throws GenericError;
	}

	interface Oven extends Device {
		idempotent OvenProgram getProgram() throws GenericError;
		void setProgram(OvenProgram program) throws GenericError;
	}


	interface Bulbulator extends Device {
		idempotent Lines getJoke() throws GenericError;
	}

	interface Wihajster extends Bulbulator {
		void setTarget(string target) throws GenericError;
		idempotent string describeTarget() throws GenericError;
	}

	interface Mykens extends Bulbulator {
		void setSpeed(int speed) throws GenericError;
		idempotent string mykensuj() throws GenericError;
	}
}
