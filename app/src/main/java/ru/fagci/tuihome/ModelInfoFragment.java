package ru.fagci.tuihome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import ru.fagci.tuihome.databinding.ModelInfoFragmentBinding;
import ru.fagci.tuihome.model.ModelObject;

public class ModelInfoFragment extends DialogFragment {
    private ModelObject modelObject;
    private ModelInfoFragmentBinding binding;

    @Override
    public void setArguments(@Nullable Bundle args) {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.model_info_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.setModel(modelObject);
    }

    public void setModel(ModelObject modelObject) {
        this.modelObject = modelObject;
    }
}
