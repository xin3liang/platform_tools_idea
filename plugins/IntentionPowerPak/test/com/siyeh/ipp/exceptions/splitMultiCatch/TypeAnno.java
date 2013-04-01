package com.siyeh.ipp.exceptions.splitMultiCatch;

import java.io.*;
import java.lang.annotation.*;

public class Simple {
  void foo() {
    try {
      Reader reader = new FileReader("");
    } <caret>catch (@A @TA IndexOutOfBoundsException | @TA FileNotFoundException e) {
    }
  }
}

@Target(ElementType.PARAMETER) @interface A { }
@Target(ElementType.TYPE_USE) @interface TA { }