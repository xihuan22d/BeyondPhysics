package com.my.models;


import java.util.List;

public class BaseModel {
    private String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return "BaseModel{" +
                "_id='" + _id + '\'' +
                '}';
    }

    public static <T extends BaseModel> boolean isNeedUpdate(List<T> baseModels, List<T> oldBaseModels) {
        boolean isNeedUpdate = false;
        if (baseModels == null) {
            return isNeedUpdate;
        }
        if (oldBaseModels == null || baseModels.size() != oldBaseModels.size()) {
            isNeedUpdate = true;
        } else {
            for (int i = 0; i < baseModels.size(); i++) {
                BaseModel baseModel = baseModels.get(i);
                boolean isHave = false;
                for (int j = 0; j < oldBaseModels.size(); j++) {
                    BaseModel oldBaseModel = oldBaseModels.get(j);
                    if (baseModel != null && oldBaseModel != null && baseModel.get_id().equals(oldBaseModel.get_id())) {
                        isHave = true;
                        break;
                    }
                }
                if (!isHave) {
                    isNeedUpdate = true;
                    break;
                }
            }
        }
        return isNeedUpdate;
    }
}

