package com.mdabdullahalshahin.memorox;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private Context context;
    private ArrayList<Note> notes;

    public NoteAdapter(Context context, ArrayList<Note> notes) {
        this.context = context;
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);

        holder.tvTitle.setText(note.getTitle());

        // short content preview
        String content = note.getContent();
        if (content.length() > 120) {
            content = content.substring(0, 120) + "...";
        }
        holder.tvContent.setText(content);

        // ============================
        // Convert UTC time → Local time
        // ============================
        String utcTime = note.getCreatedAt();
        String localFormattedTime;

        try {
            ZonedDateTime utc = ZonedDateTime.parse(utcTime);
            ZonedDateTime local = utc.withZoneSameInstant(ZoneId.systemDefault());

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("d MMM, yyyy • h:mm a");

            localFormattedTime = local.format(formatter);

        } catch (Exception e) {
            localFormattedTime = utcTime;
        }

        holder.tvTime.setText(localFormattedTime);

        // 🎨 APPLY COLOR TAG
        try {
            holder.colorTag.setBackgroundColor(Color.parseColor(note.getColor()));
        } catch (Exception e) {
            holder.colorTag.setBackgroundColor(Color.GRAY); // fallback
        }

        // open note
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddEditNoteActivity.class);
            intent.putExtra("id", note.getId());
            intent.putExtra("title", note.getTitle());
            intent.putExtra("content", note.getContent());
            intent.putExtra("created_at", note.getCreatedAt());
            intent.putExtra("color", note.getColor()); // pass color!
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvContent, tvTime;
        View colorTag;
        CardView cardView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvTime = itemView.findViewById(R.id.tvTime);
            colorTag = itemView.findViewById(R.id.colorTag); // 🎨 NEW
            cardView = (CardView) itemView;
        }
    }
}
