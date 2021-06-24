package cn.com.jaav.boot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Receiver implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String receiver;

    private String city;
}