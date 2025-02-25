package src.lab7.service;

import src.lab7.domain.Utilizator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServiceCommunity {
    private final Service service;

    public ServiceCommunity(Service service) {
        this.service = service;
    }


    public List<List<Utilizator>> Communities(){
        boolean[] visited = new boolean[service.getNewID().intValue()];
        int communityCount = 0;
        List<List<Utilizator>> communities = new ArrayList<>();

        service.getUsers().forEach(user -> {
            if (!visited[user.getId().intValue()]) {
                List<Utilizator> community = new ArrayList<>();
                dfs(user, visited, community);
                communities.add(community);
            }
        });

        return communities;
    }


    private void dfs(Utilizator user, boolean[] visited, List<Utilizator> community){
        visited[user.getId().intValue()] = true;
        community.add(user);

        service.getUserFriends(user.getId()).stream()
                .filter(u -> !visited[u.getId().intValue()])
                .forEach(u -> dfs(u, visited, community));
    }

    public List<Utilizator> mostSociableCommunity(){
        return Communities().stream()
                .max((community1, community2) -> Integer.compare(community1.size(), community2.size()))
                .orElse(new ArrayList<>());
    }
}
