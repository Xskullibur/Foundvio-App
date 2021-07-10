/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2020. All rights reserved.
 * Generated by the CloudDB ObjectType compiler.  DO NOT EDIT!
 */
package com.foundvio.model;

import com.huawei.agconnect.cloud.database.CloudDBZoneObject;
import com.huawei.agconnect.cloud.database.Text;
import com.huawei.agconnect.cloud.database.annotations.DefaultValue;
import com.huawei.agconnect.cloud.database.annotations.EntireEncrypted;
import com.huawei.agconnect.cloud.database.annotations.NotNull;
import com.huawei.agconnect.cloud.database.annotations.Indexes;
import com.huawei.agconnect.cloud.database.annotations.PrimaryKeys;

import java.util.Date;

/**
 * Definition of ObjectType TrackerTrackee.
 *
 * @since 2021-07-10
 */
@PrimaryKeys({"id"})
public final class TrackerTrackee extends CloudDBZoneObject {
    private String id;

    @NotNull
    @DefaultValue(longValue = 0L)
    private Long trackerId;

    @NotNull
    @DefaultValue(longValue = 0L)
    private Long trackeeId;

    public TrackerTrackee() {
        super(TrackerTrackee.class);
        this.trackerId = 0L;
        this.trackeeId = 0L;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTrackerId(Long trackerId) {
        this.trackerId = trackerId;
    }

    public Long getTrackerId() {
        return trackerId;
    }

    public void setTrackeeId(Long trackeeId) {
        this.trackeeId = trackeeId;
    }

    public Long getTrackeeId() {
        return trackeeId;
    }

}
