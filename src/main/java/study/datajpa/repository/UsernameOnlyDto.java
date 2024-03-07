package study.datajpa.repository;

public class UsernameOnlyDto {

    private final String username;

    public UsernameOnlyDto(String username) { //생성자의 파라미터로 필드를 정한다.
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
