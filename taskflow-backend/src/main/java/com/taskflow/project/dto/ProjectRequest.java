package com.taskflow.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectRequest {

    @NotBlank(message = "O nome do projeto não pode estar em branco")
    @Size(max = 100, message = "O nome do projeto não pode ter mais de 100 caracteres")
    private String name;

    @Size(max = 255, message = "A descrição do projeto não pode ter mais de 255 caracteres")
    private String description;
}
