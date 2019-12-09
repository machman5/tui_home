package ru.fagci.tuihome;

import android.graphics.Color;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import ru.fagci.tuihome.databinding.ListModelCategoryItemBinding;
import ru.fagci.tuihome.vm.AppViewModel;
import ru.fagci.tuihome.vm.ContactViewModel;
import ru.fagci.tuihome.vm.MediaViewModel;
import ru.fagci.tuihome.vm.ModelViewModel;


public class ModelCategoryViewHolder extends RecyclerView.ViewHolder {
    private ListModelCategoryItemBinding binding;
    private View view;

    public ModelCategoryViewHolder(ListModelCategoryItemBinding binding) {
        super(binding.getRoot());
        view = binding.getRoot();
        this.binding = binding;
    }

    private static int getColorForModelCategory(ModelViewModel viewModel) {
        if (viewModel instanceof AppViewModel) {
            return Color.rgb(128, 80, 80);
        } else if (viewModel instanceof ContactViewModel) {
            return Color.rgb(80, 128, 80);
        } else if (viewModel instanceof MediaViewModel) {
            return Color.rgb(80, 80, 128);
        } else {
            return 0;
        }
    }

    public void bind(ModelViewModel viewModel) {
        binding.setItem(viewModel);
        binding.executePendingBindings();
        binding.setLifecycleOwner((AppCompatActivity) view.getContext());
        performBind(viewModel);
    }

    private void performBind(ModelViewModel modelObject) {
        setBgColor(getColorForModelCategory(modelObject));

        view.setOnClickListener(p1 -> {

        });
    }

    private void setBgColor(int c) {
        view.setBackgroundColor(c);
    }
}
