package service;

import domain.ID;
import mapper.IdMapper;

import javax.inject.Inject;

/**
 * impl
 * Created by howen on 15/11/25.
 */
public class IdServiceImpl implements IdService{

    @Inject
    private IdMapper idMapper;


    @Override
    public ID getID(Long userId) throws Exception {
        return idMapper.getID(userId);
    }
}
