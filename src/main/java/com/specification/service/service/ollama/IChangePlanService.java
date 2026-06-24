package com.specification.service.service.ollama;

import com.specification.service.service.ollama.impl.ChangePlanServiceImpl;

import java.util.List;

public interface IChangePlanService {
    List<ChangePlanServiceImpl.Change> getChanges();
}
