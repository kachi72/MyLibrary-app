package com.systemtech.mylibrary;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class BookRecViewAdapter extends RecyclerView.Adapter<BookRecViewAdapter.ViewHolder> {

    public interface OnListUpdatedListener{
        void onListUpdated();
    }



    private ArrayList<Book> books = new ArrayList<>();

    Context context;

    public String parentActivity;
    private OnListUpdatedListener listener;

    private void updateListener(){
        if (listener != null){
            listener.onListUpdated();
        }
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    public BookRecViewAdapter(Context context, String parentActivity,OnListUpdatedListener listener) {
        this.context = context;
        this.parentActivity = parentActivity;
        this.listener = listener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_book_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(books.get(position).getTitle());
        // loading image from drawable folder
        holder.image.setImageResource(books.get(position).getImageURL());
//        // loading image using glide
//        Glide.with(context)
//                .asBitmap()
//                .load(books.get(position).getImageURL())
//                .into(holder.image);

        holder.author.setText("Author: " + books.get(position).getAuthor());
        holder.shortDesc.setText(books.get(position).getShortDesc());
        // taking care of expanded view of card
        if (books.get(position).isExpanded()) {
            TransitionManager.beginDelayedTransition(holder.card);
            holder.expandedCard.setVisibility(View.VISIBLE);
            holder.arrowDown.setVisibility(View.GONE);

        } else {
            TransitionManager.beginDelayedTransition(holder.card);
            holder.expandedCard.setVisibility(View.GONE);
            holder.arrowDown.setVisibility(View.VISIBLE);
        }
        // taking care of the delete button in the card view
        if (parentActivity.equals("AllBooksActivity")){
            holder.btnDelete.setVisibility(View.GONE);
        }
        if (parentActivity.equals("FavouriteBooksActivity")){
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position != RecyclerView.NO_POSITION) {
                        Book book = books.get(position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Are you sure you want to delete " + book.getTitle() + "?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Utils.getInstance(context).deleteFromFavourites(books.get(position))) {
                                    Toast.makeText(v.getContext(), book.getTitle() + " successfully deleted from Favourites", Toast.LENGTH_SHORT).show();
                                    book.setExpanded(!book.isExpanded());
                                    books.remove(position);
                                    notifyItemRemoved(holder.getAdapterPosition());
                                    updateListener();
                                } else {
                                    Toast.makeText(v.getContext(), "Error deleting " + book.getTitle() + " from Favourites", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        builder.create().show();
                    }
                }
            });
        }
        if (parentActivity.equals("CurrentlyReadingBooksActivity")){
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position != RecyclerView.NO_POSITION) {
                        Book book = books.get(position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Are you sure you want to delete " + book.getTitle() + "?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Utils.getInstance(context).deleteFromCurrentlyReading(books.get(position))) {
                                    Toast.makeText(v.getContext(), book.getTitle() + " successfully deleted from Currently Reading List", Toast.LENGTH_SHORT).show();
                                    book.setExpanded(!book.isExpanded());
                                    books.remove(position);
                                    notifyItemRemoved(holder.getAdapterPosition());
                                    updateListener();
                                } else {
                                    Toast.makeText(v.getContext(), "Error deleting " + book.getTitle() + " from Currently Reading List", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        builder.create().show();
                    }
                }
            });
        }
        if (parentActivity.equals("WishListActivity")){
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position != RecyclerView.NO_POSITION) {
                        Book book = books.get(position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Are you sure you want to delete " + book.getTitle() + "?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Utils.getInstance(context).deleteFromWishList(books.get(position))) {
                                    Toast.makeText(v.getContext(), book.getTitle() + " successfully deleted from WishList", Toast.LENGTH_SHORT).show();
                                    book.setExpanded(!book.isExpanded());
                                    books.remove(position);
                                    notifyItemRemoved(holder.getAdapterPosition());
                                    updateListener();
                                } else {
                                    Toast.makeText(v.getContext(), "Error deleting " + book.getTitle() + " from WishList", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        builder.create().show();
                    }
                }
            });
        }
        if (parentActivity.equals("AlreadyReadBooksActivity")){
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position != RecyclerView.NO_POSITION) {
                        Book book = books.get(position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Are you sure you want to delete " + book.getTitle() + "?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Utils.getInstance(context).deleteFromAlreadyRead(books.get(position))) {
                                    Toast.makeText(v.getContext(), book.getTitle() + " successfully deleted from Already Read List", Toast.LENGTH_SHORT).show();
                                    book.setExpanded(!book.isExpanded());
                                    books.remove(position);
                                    notifyItemRemoved(holder.getAdapterPosition());
                                    updateListener();
                                } else {
                                    Toast.makeText(v.getContext(), "Error deleting " + book.getTitle() + " from Already Read List", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        builder.create().show();

                    }
                }
            });
        }


    }

    @Override
    public int getItemCount() {

        return books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title, author, shortDesc, btnDelete;
        private RelativeLayout expandedCard;
        private MaterialCardView card;
        private ImageView image, arrowDown, arrowUp;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.bookName);
            image = itemView.findViewById(R.id.image);
            card = itemView.findViewById(R.id.bookCard);
            author = itemView.findViewById(R.id.author);
            shortDesc = itemView.findViewById(R.id.shortDesc);
            arrowDown = itemView.findViewById(R.id.imgArrowDown);
            arrowUp = itemView.findViewById(R.id.imgArrowUp);
            expandedCard = itemView.findViewById(R.id.expandedCardView);
            btnDelete = itemView.findViewById(R.id.txtDelete);


            arrowDown.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // get the item that you are clicking on
                    Book book = books.get(getAdapterPosition());
                    book.setExpanded(!book.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
            arrowUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Book book = books.get(getAdapterPosition());
                    book.setExpanded(!book.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Context context1 = v.getContext();
                        String bookTitle = books.get(position).getTitle();
                        Log.d("Card onClick", "Book title: " + bookTitle);
                        Toast.makeText(context1, bookTitle + " selected", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context1, BookActivity.class);
                        intent.putExtra("title", bookTitle);
                        intent.putExtra("parentActivity", parentActivity);
                        context1.startActivity(intent);
                    }
                }
            });

        }
    }


}

