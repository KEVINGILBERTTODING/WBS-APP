package com.example.wbs.features.chat.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.wbs.core.models.ResponseApiModel;
import com.example.wbs.features.chat.repositories.ChatRepository;

import java.util.HashMap;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;


@HiltViewModel
public class ChatViewModel extends ViewModel {
    private ChatRepository chatRepository;
    @Inject

    public ChatViewModel(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public LiveData<ResponseApiModel> storeChat(boolean isPengadu, HashMap<String, Object> data) {
        return chatRepository.storeChat(isPengadu, data);
    }
}
