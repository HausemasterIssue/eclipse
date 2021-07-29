package xyz.aesthetical.astra.managers.friends;

import java.util.ArrayList;
import java.util.UUID;

public class FriendManager {
    private final ArrayList<Friend> friends = new ArrayList<>();

    public void add(Friend friend) {
        friends.add(friend);
    }

    public void remove(Friend friend) {
        friends.remove(friend);
    }

    public boolean isFriend(UUID uuid) {
        return friends.stream().anyMatch(friend -> friend.getUuid().equals(uuid));
    }

    public Friend getFriend(UUID uuid) {
        for (Friend friend : friends) {
            if (friend.getUuid().equals(uuid)) {
                return friend;
            }
        }

        return null;
    }

    public ArrayList<Friend> getFriends() {
        return friends;
    }
}
