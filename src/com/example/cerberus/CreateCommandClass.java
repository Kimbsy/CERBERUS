package com.example.cerberus;

import java.util.Random;

import android.os.AsyncTask;
import android.util.Log;

public class CreateCommandClass extends AsyncTask<String, Integer, String> {

	/**
	 * Asynchronous task keeps networking out of UI thread
	 */
	protected String doInBackground(String... match) {
		createCommand(match[0]);
		return "good";
	}

	/**
	 * String combinations for various commands
	 */
	// start fire
	private String[][] fireOn = {
			{ "fire", "on" },
			{ "light", "fire" },
			{ "start", "fire" },
			{ "get", "fire", "going" },
			{ "feeling", "cold" },
	};
	// stop fire
	private String[][] fireOff = {
			{ "fire", "off" },
			{ "fire", "out" },
			{ "too", "hot" },
	};
	// add weight
	private String[][] addWeight = {
			{ "beth", "weigh" },
			{ "best", "weigh" },
			{ "dave", "weight" },
			{ "beth", "wait" },
			{ "best", "wait" },
			{ "dave", "wait" },
			{ "beth", "way" },
			{ "best", "way" },
			{ "dave", "way" },
	};
	// showWeightGraph
	private String[][] showWeightGraph = {
			{ "show", "weigh" },
			{ "show", "wait" },
			{ "show", "way" },
	};
	// closeWeightGraph
	private String[][] closeWeightGraph = {
			{ "close", "graph" },
			{ "close", "grass" },
			{ "clover", "graph" },
			{ "clover", "grass" },
			{ "put", "away" },
	};
	// show ISS stream
	private String[][] showSpaceCam = {
			{ "show", "world" },
			{ "show", "space" },
			{ "show", "everything" },
			{ "show", "state" },
	};
	// close ISS stream
	private String[][] closeSpaceCam = {
			{ "enough", "space" },
			{ "turn", "off" },
			{ "close" },
			{ "clothes" },
	};
	// thank you
	private String[][] thanks = {
			{ "thanks" },
			{ "thank you" },
			{ "cheers" },
	};

	/**
	 * Randomized responses
	 */
	Random rand = new Random();
	private String[] responses = {
		"of course",
		"sure thing",
		"righty ho",
		"absolutely",
		"it would be my pleasure",
		"i was just about to suggest that",
		"if you say so", "okay",
		"just for yoo",
		"already on it",
	};

	/**
	 * Test to see if keywords from phrase occur in matched words
	 * 
	 * @param words
	 * @param phrases
	 * @return
	 */
	public boolean checkString(String words, String[][] phrases) {

		// loop through all phrases
		for (int i = 0; i < phrases.length; i++) {
			// assume it's not a match
			boolean isMatch = true;

			// loop through all words in phrase
			for (int j = 0; j < phrases[i].length; j++) {
				if (!words.toLowerCase().contains(phrases[i][j].toLowerCase())) {
					isMatch = false;
				}
			}

			if (isMatch) {
				return true;
			}
		}

		return false;
	}

	/**
	 * decide which command to execute and construct it
	 * 
	 * @param words
	 */
	public void createCommand(String words) {

		String response = responses[rand.nextInt(responses.length)];

		/**
		 * TURN FIRE ON
		 */
		if (checkString(words, fireOn)) {
			Log.i("command", "Turning fire on...");

			// create commands to export display and play video
			String[] commands = {
					"espeak -a 200 '" + response + "'",
					"export DISPLAY=:0.0 && nohup vlc --fullscreen --repeat Videos/fireplace.mp4 &",
			};

			// execute commands
			sendCommand(commands);
		}
		/**
		 * TURN FIRE OFF
		 */
		else if (checkString(words, fireOff)) {
			Log.i("command", "Turning fire off...");

			// create commands to kill vlc process
			String[] commands = { "espeak -a 200 '" + response + "'",
					"pkill vlc" };

			// execute commands
			sendCommand(commands);
		}
		/**
		 * RECORD WEIGHT
		 */
		else if (checkString(words, addWeight)) {
			Log.i("command", "Adding weight...");

			String user = "";

			String weight = getWeight(words);

			if (words.toLowerCase().contains("dave")) {
				user = "dave";
			} else if (words.toLowerCase().contains("beth")
					|| words.toLowerCase().contains("best")) {
				user = "beth";
			}

			if (user != "" && weight != "") {
				Log.i("command", "user: " + user + ", weight: " + weight);

				// create commands to create newData file with inputs,
				// then run Python script to import data and delete newData file
				String[] commands = {
						"espeak 'user: " + user + ", weight: " + weight + "'",
						"cd CERBERUS/weight && " + "echo '" + user + ","
						+ weight + "' > newData.csv && "
						+ "python inputWeight.py",
				};

				// execute commands
				sendCommand(commands);
			} else {
				Log.i("command", "unknown user or weight");
			}
		}
		/**
		 * SHOW WEIGHT GRAPH
		 */
		else if (checkString(words, showWeightGraph)) {
			Log.i("command", "Showing weight graph...");

			// create command to open chromium at specified web page in
			// fullscreen
			// python http server is required for d3
			String[] commands = {
					"espeak -a 200 '" + response + "'",
					// "python -m SimpleHTTPServer & " +
					"export DISPLAY=:0.0 && "
					+ "chromium-browser --kiosk 'http://localhost:8000/weight/weight.html'",
			};

			// execute commands
			sendCommand(commands);
		}
		/**
		 * CLOSE WEIGHT GRAPH
		 */
		else if (checkString(words, closeWeightGraph)) {
			Log.i("command", "Closing weight graph...");

			// create command to close chromium window and shut down any http
			// servers
			String[] commands = {
					"espeak -a 200 '" + response + "'",
					"export DISPLAY=:0.0 && " + "pkill chromium",
			};

			// execute commands
			sendCommand(commands);
		}
		/**
		 * Show Camera feed form ISS
		 */
		else if (checkString(words, showSpaceCam)) {
			Log.i("command", "Showing camera feed...");
			
			// create command to display ISS camera feed using mplayer
			String[] commands = {
					"espeak -a 200 '" + response + "'",
//					WHY U NO WORK????
//					"export DISPLAY=:0.0 && nohup mplayer -fs -stop-xscreensaver Videos/world.m3u8 &",
					"export DISPLAY=:0.0 && mplayer -fs -stop-xscreensaver Videos/world.m3u8",
			};
			
			// execute commands
			sendCommand(commands);
		}
		/**
		 * Close ISS stream
		 */
		else if (checkString(words, closeSpaceCam)) {
			Log.i("command", "Closing camera feed...");
			
			// create command to close mplayer
			String[] commands = {
					"espeak -a 200 '" + response + "'",
					"pkill mplayer",
			};
		}
		/*
		 */
		/**
		 * SAY THANKS
		 */
		else if (checkString(words, thanks)) {
			Log.i("command", "Receiving thanks...");

			// create commands to kill vlc process
			String[] commands = { "espeak -a 200 'your welcome'" };

			// execute commands
			sendCommand(commands);
		}
		/**
		 * UNKNOWN COMMAND
		 */
		else {
			Log.i("command", "phrase not recognised");

			// create command to say it doesnt know what to do
			String[] commands = {};
			if (rand.nextBoolean()) {
				commands = {
					"espeak -a 200 'i dont know how to "
					+ transposePossesives(words) + "'",
				};
			}
			else {
				commands = {
					"espeak -a 200 'im sorry dave. im afraid i cant do that'",
				};
			}

			// execute commands
			sendCommand(commands);
		}
	}

	/**
	 * Get number from string input
	 * 
	 * @param words
	 * @return
	 */
	String getWeight(String words) {
		// remove all characters that aren't digits or decimal points
		String out = words.replaceAll("[^\\d.]", "");

		return out;
	}

	/**
	 * change instances of possesive pronouns
	 * 
	 * @param words
	 * @return
	 */
	public String transposePossesives(String words) {

		// add spaces before and after
		words = " " + words + " ";

		String transpose = words.replaceAll(" i ", " YOU ");
		transpose = transpose.replaceAll(" me ", " YOU ");
		transpose = transpose.replaceAll(" my ", " YOUR ");
		transpose = transpose.replaceAll(" you ", " ME ");
		transpose = transpose.replaceAll(" us ", " YOU ");
		transpose = transpose.replaceAll(" our ", " YOUR ");
		transpose = transpose.replaceAll(" we ", " YOU ");

		return transpose;
	}

	/**
	 * Send command via SSHto remote machine to be executed
	 * 
	 * @param commands
	 */
	public void sendCommand(String... commands) {
		String userName = "USERNAME";
		String pass = "PASSWORD";
		String connectionIP = "IP_ADDRESS";

		SSHManager instance = new SSHManager(userName, pass, connectionIP, "",
				22);

		String errorMessage = instance.connect();

		if (errorMessage != null) {
			Log.i("SSHManager", "connection failed: " + errorMessage);
			// fail();
		}

		String expResult = "FILE_NAME\n";

		// call sendCommand() for each command and the output (without prompts)
		// is returned
		String result[] = new String[commands.length];

		for (int i = 0; i < commands.length; i++) {
			result[i] = instance.sendCommand(commands[i]);
			Log.i("SSHManager", result[i]);
		}

		// close after all commands are sent
		instance.close();
	}
}
