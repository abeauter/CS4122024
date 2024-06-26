public class User {
    private String username;
    private String password;
    private int score;

    public User(String username, String password, int score) {
        this.username = username;
        this.password = password;
        this.score = score;
    }

    @Override
    public String toString() {
        return String.format("%s %s", username, score);
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getPassword() {
        return password;
    }
}
