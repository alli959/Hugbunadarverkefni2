package yolo.basket;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.HashMap;

import yolo.basket.db.player.Player;
import yolo.basket.db.stats.Stats;
import yolo.basket.db.team.Team;
import yolo.basket.util.StatsFormatter;

public class StatsViewDialog {

    private final Context context;
    private final StatsFormatter formatter;
    private Stats stats;
    private View profileView;

    public StatsViewDialog(Context context, Stats stats, Player player) {
        this(context, stats, player.getGamesPlayed().size());
        createProfileView(player);
    }

    public StatsViewDialog(Context context, Stats stats, Team team) {
        this(context, stats, team.getGamesPlayed().size());
        createProfileView(team);
    }

    private StatsViewDialog(Context context, Stats stats, int gamesPlayed) {
        this.context = context;
        this.stats = stats;
        this.formatter = new StatsFormatter(stats, gamesPlayed);
    }

    int FILL_PARENT = -1;
    public void createDialog() {
        Dialog dialog = new Dialog(context);
        dialog.setTitle("Statistics");


        ScrollView mainLayout = new ScrollView(context);
        LinearLayout content = new LinearLayout(context);

        mainLayout.addView(content);

        View summaryView = createSummaryView();
        View statsView = createStatsView();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(FILL_PARENT, 1400);
        params.weight = 1;

        summaryView.setLayoutParams(params);
        statsView.setLayoutParams(params);

        content.addView(summaryView);
        content.addView(statsView);

        dialog.setContentView(mainLayout);
        dialog.setCancelable(true);

        dialog.getWindow().setLayout(FILL_PARENT, FILL_PARENT);
        dialog.show();
    }

    private View createSummaryView() {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        layout.addView(profileView);
        layout.addView(summaryStatsView());
        return layout;
    }

    private void createProfileView(Team team) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(createTextView(team.getName(), 50));
        layout.addView(createPlayerListView(team));
        profileView = layout;
    }

    private View createPlayerListView(Team team) {
        ListView playerListView = new ListView(context);
        String[] playerNames = team.getPlayers().stream().map(Player::getName).toArray(String[]::new);
        ArrayAdapter<String> playerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, playerNames);
        playerListView.setAdapter(playerAdapter);
        return playerListView;
    }

    private TextView createTextView(String text, float px) {
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setTextSize(px);
        return textView;
    }

    private void createProfileView(Player player) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(createTextView(player.getName(), 18));
        layout.addView(createTextView(player.getPlayerPos(), 12));
        layout.addView(createTextView(String.valueOf(player.getPlayerNr()), 12));
        profileView = layout;
    }

    private View summaryStatsView() {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        for (HashMap.Entry<String, String> entry : formatter.getSummaryMap().entrySet())
            layout.addView(createSummaryStatsRow(entry.getKey(), entry.getValue()));
        return layout;
    }

    private View createSummaryStatsRow(String headerText, String dataText) {
        View row = LayoutInflater.from(context).inflate(R.layout.summary_stats_row, null);

        TextView header = row.findViewById(R.id.summary_stats_row_header);
        TextView data = row.findViewById(R.id.summary_stats_row_info);

        header.setText(headerText);
        data.setText(dataText);

        return row;
    }

    private View createStatsView() {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        layout.addView(createTextView("Scored by location", 30));
        layout.addView(createLocationStatsView());

        layout.addView(createTextView("Scored by points", 30));
        layout.addView(createPointsStatsView());
        return layout;
    }

    private View createLocationStatsView() {
        return createTableViewFromHashMap(formatter.getHitsByLocationMap());
    }

    private View createPointsStatsView() {
        return createTableViewFromHashMap(formatter.pointsMap());
    }

    private View createTableViewFromHashMap(HashMap<String, String[]> map) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(createTableRow("", new String[]{"Total", "Accuracy %"}));
        for (HashMap.Entry<String, String[]> entry : map.entrySet())
            layout.addView(createTableRow(entry.getKey(), entry.getValue()));
        return layout;
    }

    private View createTableRow(String headerText, String[] values) {
        View view = LayoutInflater.from(context).inflate(R.layout.stats_table_row, null);
        LinearLayout row = view.findViewById(R.id.stats_table_row);

        TextView rowHeader = row.findViewById(R.id.stats_table_row_header);
        TextView data0 = row.findViewById(R.id.stats_table_row_data0);
        TextView data1 = row.findViewById(R.id.stats_table_row_data1);

        rowHeader.setText(headerText);
        data0.setText(values[0]);
        data1.setText(values[1]);

        return view;
    }

    private View createTableEntry(String value) {
        View entry = LayoutInflater.from(context).inflate(R.layout.stats_table_entry,  null);
        TextView rowEntry = entry.findViewById(R.id.stats_table_entry);
        rowEntry.setText(value);
        return entry;
    }


}
