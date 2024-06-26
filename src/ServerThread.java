import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ServerThread extends Server implements Runnable {
    private Socket clientSocket;
    private String currentUsername;
    private PrintWriter printWriter;
    private boolean isOpen = true;

    public ServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
        System.out.println("Connected to client " + clientSocket);
    }

    public void close() {
        if(currentUsername != null) loginManager.logout(currentUsername);
        System.out.println("Disconnecting from client " + clientSocket);
        printWriter.print("CLOSE");
        printWriter.flush();
        printWriter.close();
        try {
            clientSocket.close();
            isOpen = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public void run() {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            printWriter = new PrintWriter(clientSocket.getOutputStream());

            String input;
            while(!clientSocket.isClosed() && (input = bufferedReader.readLine()) != null) {
                String response = handleInput(input);
                printWriter.println(response);
                printWriter.flush();
            }
        } catch (SocketException e) {
            System.out.println("ERROR: SocketException (If you're seeing this while purposefully terminating " +
                    "the server, disregard.)");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private String handleInput(String input) {
        try {
            if(input.equals("FLIP")) {
                return CoinFlipGame.flipCoin();
            } else if(input.equals("ROLL")) {
                return DiceRollGame.rollDice();
            } else if(input.equals("LEADERBOARD")) {
                return requestLeaderboardUpdate();
            } else {
                String[] splitInput = input.split(" ");
                return parseLongInput(splitInput);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String parseLongInput(String[] splitInput) {
        try {
            switch(splitInput[0]) {
                case "LOGIN":
                    User user = loginManager.login(splitInput[1], splitInput[2]);
                    if(user != null) {
                        currentUsername = user.getUsername();
                        return ("TRUE " + user);
                    } else {
                        return "FALSE";
                    }
                case "LOGOUT":
                    loginManager.logout(splitInput[1]);
                    return "LOGOUT_SUCCESS";
                case "CREATE":
                    if(loginManager.createAccount(splitInput[1], splitInput[2])) {
                        return "TRUE";
                    } else {
                        return "FALSE";
                    }
                case "ADD":
                    String username = splitInput[1];
                    int change = 1 + Integer.parseInt(splitInput[2]);
                    return requestScoreUpdate(username, change);
                case "LOSE":
                    username = splitInput[1];
                    change = -1 * (Integer.parseInt(splitInput[2]));
                    return requestScoreUpdate(username, change);
                default:
                    return "INVALID";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String requestScoreUpdate(String username, int change) {
        try {
            User user = database.getUser(username);
            int newScore = user.getScore() + change;
            database.updateScore(username, newScore);
            return String.format("SCORE %d", newScore);
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String requestLeaderboardUpdate() {
        try {
            LeaderboardUpdater leaderboardUpdater = new LeaderboardUpdater(database);
            ArrayList<User> users = leaderboardUpdater.getTopThreePlayers();
            StringBuilder response = new StringBuilder("LEADERBOARD ");
            for(User u : users) {
                response.append(u).append(",");
            }
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }
}
