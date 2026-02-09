package com.lms.www.controller.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPermissionRequest {
    private List<String> permissions;
}
