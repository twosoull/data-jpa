package study.datajpa.repository;

public interface NestedColosedProjections {

    String getUsername();
    TeamInfo getTeam();

    interface TeamInfo{
        String getName();
    }
}
