package cn.com.jaav.boot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Weather
{
    private String date;

    private String weather;

    private String temp;

    private String wind;
}