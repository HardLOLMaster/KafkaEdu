package com.github.hlam.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Person
{
    @NotNull
    private String name;
    private String email;
    private Integer year;
}
