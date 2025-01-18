package zone.vao.nexoAddon.classes.mechanic;

import lombok.Getter;

@Getter
public class KillMessage {
    private final String deathMessage;

    public KillMessage(final String deathMessage) {
        this.deathMessage = deathMessage;
    }
}
