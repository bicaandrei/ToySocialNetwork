package Service;

import Domain.Friendship;
import Domain.User;
import Repository.CommunitiesRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommunitiesService {

    private CommunitiesRepository repository;

    public CommunitiesService(CommunitiesRepository repository) { this.repository = repository; }

    public void addUserToCommunity(User user) { this.repository.add_user(user.getId()); }

    public void addFriendshipToCommunity(Friendship friendship) { this.repository.add_friendship(friendship.get_e1().getId(), friendship.get_e2().getId()); }

    public void removeUserFromCommunities(Long id){

        this.repository.remove_user_from_communities(id);

    }
    public Set<Long> getBiggestCommunity() {

        Map<Long, Set<Long>> communities = this.repository.get_communites();
       // if(communities == null)
       //     return null;
        int MaxSize = Integer.MIN_VALUE;
        Long key = -1L;
        for(Long community : communities.keySet()){

            Set<Long> usersInCommunity = communities.get(community);
            if(usersInCommunity.size() > MaxSize){

                MaxSize = usersInCommunity.size();
                key = community;

            }

        }

        if(MaxSize > 1)
           return communities.get(key);
        else
           return null;
    }

    public Long getNumberOfCommunities() {

        Map<Long, Set<Long>> communities = this.repository.get_communites();
        Long nr = 0L;
        for (Long community : communities.keySet()) {

            Set<Long> usersInCommunity = communities.get(community);
            if (usersInCommunity.size() > 1) {

                nr += 1;

            }

        }

        return nr;

    }

}
