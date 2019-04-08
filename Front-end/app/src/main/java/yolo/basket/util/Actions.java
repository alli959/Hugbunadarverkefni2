package yolo.basket.util;

import java.util.Arrays;

import yolo.basket.db.gameEvent.GameEvent;

public enum Actions {

        Scored(GameEvent.HIT),
        Missed(GameEvent.MISS),
        Foul(GameEvent.FOUL),
        Turnover(GameEvent.TURNOVER),
        Rebound(GameEvent.REBOUND),
        Block(GameEvent.BLOCK),
        Assist(GameEvent.ASSIST);

        int action;
        String message = this.name();

        Actions(int action) {
            this.action = action;
        }

        Actions(int action, String message) {
                this(action);
                this.message = message;
        }

        public int getAction() {
                return action;
        }

        public static CharSequence[] getNames() {
            return Arrays.stream(Actions.class.getEnumConstants()).map(Enum::name).toArray(CharSequence[]::new);
        }

}
