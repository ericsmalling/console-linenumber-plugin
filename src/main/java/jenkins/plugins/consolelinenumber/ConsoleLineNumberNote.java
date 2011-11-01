/*
 * The MIT License
 * 
 * Copyright (c) 2011 Eric A. Smalling
 * Based on http://wiki.jenkins-ci.org/display/JENKINS/Timestamper by Steven G. Brown
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package jenkins.plugins.consolelinenumber;

import hudson.MarkupText;
import hudson.console.ConsoleAnnotator;
import hudson.console.ConsoleNote;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Line number note that is inserted into the console output.
 * 
 * @author Eric A. Smalling
 */
public final class ConsoleLineNumberNote extends ConsoleNote<Object> {

  /**
   * Serialization UID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * current line number.
   */
  private final String lineNumber;

  /**
   * Create a new {@link ConsoleLineNumberNote}.
   * 
   * @param lineNumber
   *          milliseconds since the epoch
   */
  ConsoleLineNumberNote(String lineNumber) {
    this.lineNumber = lineNumber;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ConsoleAnnotator<Object> annotate(Object context, MarkupText text,
      int charPos) {
    // Add as end tag, which will be inserted prior to tags added by other
    // console notes (e.g. AntTargetNote).
    text.addMarkup(0, 0, "", "<a name=\""+lineNumber.trim()+"\"><b>" + lineNumber + "</b></a>  ");
    return null;
  }
}
