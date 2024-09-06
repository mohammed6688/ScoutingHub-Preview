package com.krnal.products.scoutinghub.service;

import java.io.IOException;

public interface UpdateHelper<M, D> {
    void set(M model, D dto) throws IOException;
}
