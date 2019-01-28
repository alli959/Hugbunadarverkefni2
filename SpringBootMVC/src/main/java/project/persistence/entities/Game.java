package project.persistence.entities;


import javax.persistence.*;


import static javax.persistence.GenerationType.*;


/*
    This is a huge database table for the shots, hits or miss
 */
@Entity
@Table(name = "game")
public class Game {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private boolean isBench;
    private Long turnover;
    private Long block;
    private Long steal;
    private Long foul;
    private Long assist;
    private Long rebound;
    private Long playerId;
    private Long LeftWingThreeHit;
    private Long LeftWingThreeMiss;
    private Long RightWingThreeHit;
    private Long RightWingThreeMiss;
    private Long TopThreeHit;
    private Long TopThreeMiss;
    private Long LeftCornerThreeHit;
    private Long LeftCornerThreeMiss;
    private Long RightCornerThreeHit;
    private Long RightCornerThreeMiss;
    private Long LeftShortCornerHit;
    private Long LeftShortCornerMiss;
    private Long RightShortCornerHit;
    private Long RightShortCornerMiss;
    private Long LeftTopKeyHit;
    private Long LeftTopKeyMiss;
    private Long RightTopKeyHit;
    private Long RightTopKeyMiss;
    private Long TopKeyHit;
    private Long TopKeyMiss;
    private Long LayUpHit;
    private Long LayUpMiss;
    private Long FreeThrowHit;
    private Long FreeThrowMiss;

    public Game() {

    }

    public Game(Long id, Long turnover, Long playerId, boolean isBench, Long block, Long steal, Long foul, Long assist, Long rebound, Long LeftWingThreeHit, Long LeftWingThreeMiss,
                Long RightWingThreeHit, Long RightWingThreeMiss, Long TopThreeHit, Long TopThreeMiss,
                Long LeftCornerThreeHit, Long LeftCornerThreeMiss, Long RightCornerThreeHit, Long RightCornerThreeMiss,
                Long LeftShortCornerHit, Long LeftShortCornerMiss, Long RightShortCornerHit, Long RightShortCornerMiss, Long LeftTopKeyHit,
                Long LeftTopKeyMiss, Long RightTopKeyHit, Long RightTopKeyMiss, Long TopKeyHit, Long TopKeyMiss, Long LayUpHit, Long LayUpMiss, Long freeThrowHit, Long freeThrowMiss) {

        this.id = id;
        this.turnover = turnover;
        this.playerId = playerId;
        this.isBench = isBench;
        this.block = block;
        this.steal = steal;
        this.foul = foul;
        this.assist = assist;
        this.rebound = rebound;
        this.LeftWingThreeHit = LeftWingThreeHit;
        this.LeftWingThreeMiss = LeftWingThreeMiss;
        this.RightWingThreeHit = RightWingThreeHit;
        this.RightWingThreeMiss = RightWingThreeMiss;
        this.TopThreeHit = TopThreeHit;
        this.TopThreeMiss = TopThreeMiss;
        this.LeftCornerThreeHit = LeftCornerThreeHit;
        this.LeftCornerThreeMiss = LeftCornerThreeMiss;
        this.RightCornerThreeHit = RightCornerThreeHit;
        this.RightCornerThreeMiss = RightCornerThreeMiss;
        this.LeftShortCornerHit = LeftShortCornerHit;
        this.LeftShortCornerMiss = LeftShortCornerMiss;
        this.RightShortCornerHit = RightShortCornerHit;
        this.RightShortCornerMiss = RightShortCornerMiss;
        this.LeftTopKeyHit = LeftTopKeyHit;
        this.LeftTopKeyMiss = LeftTopKeyMiss;
        this.RightTopKeyHit = RightTopKeyHit;
        this.RightTopKeyMiss = RightTopKeyMiss;
        this.TopKeyHit = TopKeyHit;
        this.TopKeyMiss = TopKeyMiss;
        this.LayUpHit = LayUpHit;
        this.LayUpMiss = LayUpMiss;

        FreeThrowHit = freeThrowHit;
        FreeThrowMiss = freeThrowMiss;
    }

    public Long getId() {
        return id;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public boolean isBench() {
        return isBench;
    }

    public Long getLeftWingThreeHit() {
        return LeftWingThreeHit != null ? LeftWingThreeHit:0;
    }

    public Long getLeftWingThreeMiss() {
        return LeftWingThreeMiss != null ? LeftWingThreeMiss:0;
    }

    public Long getRightWingThreeHit() {
       return RightWingThreeHit != null ? RightWingThreeHit:0;

    }

    public Long getRightWingThreeMiss() {
        return RightWingThreeMiss != null ? RightWingThreeMiss:0;

    }

    public Long getTopThreeHit() {
       return TopThreeHit != null ? TopThreeHit:0;

    }

    public Long getTopThreeMiss() {
      return TopThreeMiss != null ? TopThreeMiss:0;

    }

    public Long getLeftCornerThreeHit() {
        return LeftCornerThreeHit != null ? LeftCornerThreeHit:0;

    }

    public Long getLeftCornerThreeMiss() {
        return LeftCornerThreeMiss != null ? LeftCornerThreeMiss:0;

    }

    public Long getRightCornerThreeHit() {
        return RightCornerThreeHit != null ? RightCornerThreeHit:0;

    }

    public Long getRightCornerThreeMiss() {
        return RightCornerThreeMiss != null ? RightCornerThreeMiss:0;

    }

    public Long getLeftShortCornerHit() {
        return LeftShortCornerHit != null ? LeftShortCornerHit:0;

    }

    public Long getLeftShortCornerMiss() {
        return LeftShortCornerMiss != null ? LeftShortCornerMiss:0;

    }

    public Long getRightShortCornerHit() {
        return RightShortCornerHit != null ? RightShortCornerHit:0;

    }

    public Long getRightShortCornerMiss() {
        return RightShortCornerMiss != null ? RightShortCornerMiss:0;

    }

    public Long getLeftTopKeyHit() {
        return LeftTopKeyHit != null ? LeftTopKeyHit:0;

    }

    public Long getLeftTopKeyMiss() {
        return LeftTopKeyMiss != null ? LeftTopKeyMiss:0;

    }

    public Long getRightTopKeyHit() {
        return RightTopKeyHit != null ? RightTopKeyHit:0;

    }

    public Long getRightTopKeyMiss() {
        return RightTopKeyMiss != null ? RightTopKeyMiss:0;

    }

    public Long getTopKeyHit() {
        return TopKeyHit != null ? TopKeyHit:0;

    }

    public Long getTopKeyMiss() {
        return TopKeyMiss != null ? TopKeyMiss:0;

    }

    public Long getLayUpHit() {
        return LayUpHit != null ? LayUpHit:0;

    }

    public Long getLayUpMiss() {
        return LayUpMiss != null ? LayUpMiss:0;

    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public void setBench(boolean bench) {
        isBench = bench;
    }

    public void setLeftWingThreeHit(Long leftWingThreeHit) {
        LeftWingThreeHit = leftWingThreeHit;
    }

    public void setLeftWingThreeMiss(Long leftWingThreeMiss) {
        LeftWingThreeMiss = leftWingThreeMiss;
    }

    public void setRightWingThreeHit(Long rightWingThreeHit) {
        RightWingThreeHit = rightWingThreeHit;
    }

    public void setRightWingThreeMiss(Long rightWingThreeMiss) {
        RightWingThreeMiss = rightWingThreeMiss;
    }

    public void setTopThreeHit(Long topThreeHit) {
        TopThreeHit = topThreeHit;
    }

    public void setTopThreeMiss(Long topThreeMiss) {
        TopThreeMiss = topThreeMiss;
    }

    public void setLeftCornerThreeHit(Long leftCornerThreeHit) {
        LeftCornerThreeHit = leftCornerThreeHit;
    }

    public void setLeftCornerThreeMiss(Long leftCornerThreeMiss) {
        LeftCornerThreeMiss = leftCornerThreeMiss;
    }

    public void setRightCornerThreeHit(Long rightCornerThreeHit) {
        RightCornerThreeHit = rightCornerThreeHit;
    }

    public void setRightCornerThreeMiss(Long rightCornerThreeMiss) {
        RightCornerThreeMiss = rightCornerThreeMiss;
    }

    public void setLeftShortCornerHit(Long leftShortCornerHit) {
        LeftShortCornerHit = leftShortCornerHit;
    }

    public void setLeftShortCornerMiss(Long leftShortCornerMiss) {
        LeftShortCornerMiss = leftShortCornerMiss;
    }

    public void setRightShortCornerHit(Long rightShortCornerHit) {
        RightShortCornerHit = rightShortCornerHit;
    }

    public void setRightShortCornerMiss(Long rightShortCornerMiss) {
        RightShortCornerMiss = rightShortCornerMiss;
    }

    public void setLeftTopKeyHit(Long leftTopKeyHit) {
        LeftTopKeyHit = leftTopKeyHit;
    }

    public void setLeftTopKeyMiss(Long leftTopKeyMiss) {
        LeftTopKeyMiss = leftTopKeyMiss;
    }

    public void setRightTopKeyHit(Long rightTopKeyHit) {
        RightTopKeyHit = rightTopKeyHit;
    }

    public void setRightTopKeyMiss(Long rightTopKeyMiss) {
        RightTopKeyMiss = rightTopKeyMiss;
    }

    public void setTopKeyHit(Long topKeyHit) {
        TopKeyHit = topKeyHit;
    }

    public void setTopKeyMiss(Long topKeyMiss) {
        TopKeyMiss = topKeyMiss;
    }

    public void setLayUpHit(Long layUpHit) {
        LayUpHit = layUpHit;
    }

    public void setLayUpMiss(Long layUpMiss) {
        LayUpMiss = layUpMiss;
    }

    public Long getTurnover() {
        return turnover != null ? turnover:0;
    }

    public void setTurnover(Long turnover) {
        this.turnover = turnover;
    }

    public Long getBlock() {
        return block != null ? block:0;

    }

    public void setBlock(Long block) {
        this.block = block;
    }

    public Long getSteal() {
        return steal != null ? steal:0;

    }

    public void setSteal(Long steal) {
        this.steal = steal;
    }

    public Long getFoul() {
        return foul != null ? foul:0;

    }

    public void setFoul(Long foul) {
        this.foul = foul;
    }

    public Long getAssist() {
        return assist != null ? assist:0;

    }

    public void setAssist(Long assist) {
        this.assist = assist;
    }

    public Long getRebound() {
        return rebound != null ? rebound:0;
    }

    public void setRebound(Long rebound) {
        this.rebound = rebound;
    }

    public Long getFreeThrowHit() {
        return FreeThrowHit != null ? FreeThrowHit:0;
    }

    public void setFreeThrowHit(Long freeThrowHit) {
        FreeThrowHit = freeThrowHit;
    }

    public Long getFreeThrowMiss() {
        return FreeThrowMiss != null ? FreeThrowMiss:0;

    }

    public void setFreeThrowMiss(Long freeThrowMiss) {
        FreeThrowMiss = freeThrowMiss;
    }
}
