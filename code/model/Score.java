package model;

public class Score {

    private String nick_name;
    private String date;
    private String score;

    public Score(String nick_name, String date, String score ) {
		this.nick_name = nick_name;
		this.date=date;
		this.score=score;
    }

    public String getNickName() {
        return nick_name;
    }

	public String getDate() {
		return date;
	}
	
	public String getScore() {
		return score;
	}

	public String toString() {
		return nick_name + "\t" + date + "\t" + score;
	}

}
