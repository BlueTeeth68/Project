package com.mangareader.web.rest.vm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeChapterVM {

    Long id;

    String name;

    Float chapterNumber;

}
