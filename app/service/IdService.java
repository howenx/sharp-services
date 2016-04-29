package service;

import domain.ID;

/**
 * Id service
 * Created by howen on 15/11/25.
 */
public interface IdService {

    ID getID(Long userId) throws Exception;
}
