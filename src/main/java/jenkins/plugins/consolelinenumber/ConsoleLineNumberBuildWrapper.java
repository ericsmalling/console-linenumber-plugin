/*
 * The MIT License
 * 
 * Copyright (c) 2011 Eric A. Smalling
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

import hudson.Extension;
import hudson.Launcher;
import hudson.console.LineTransformationOutputStream;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildWrapper;
import hudson.tasks.BuildWrapperDescriptor;

import java.io.IOException;
import java.io.OutputStream;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Build wrapper that decorates the build's logger to insert a
 * {@link ConsoleLineNumberNote} on each output line.
 * 
 * @author Eric A. Smalling
 */
public final class ConsoleLineNumberBuildWrapper extends BuildWrapper {

  /**
   * Create a new {@link ConsoleLineNumberBuildWrapper}.
   */
  @DataBoundConstructor
  public ConsoleLineNumberBuildWrapper() {
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public Environment setUp(AbstractBuild build, Launcher launcher,
      BuildListener listener) throws IOException, InterruptedException {
    // Jenkins requires this method to be overridden.
    return new Environment() {
    };
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public OutputStream decorateLogger(AbstractBuild build, OutputStream logger) {
    return new LineNumberOutputStream(logger);
  }

  /**
   * Output stream that writes each line to the provided delegate output stream
   * after inserting a {@link ConsoleLineNumberNote}.
   */
  private static class LineNumberOutputStream extends
      LineTransformationOutputStream {

      private long currentLogLineNumber = 0;

    /**
     * The delegate output stream.
     */
    private final OutputStream delegate;

    /**
     * Create a new {@link jenkins.plugins.consolelinenumber.ConsoleLineNumberBuildWrapper.LineNumberOutputStream}.
     * 
     * @param delegate
     *          the delegate output stream
     */
    private LineNumberOutputStream(OutputStream delegate) {
      this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void eol(byte[] b, int len) throws IOException {
      new ConsoleLineNumberNote(padded(++currentLogLineNumber)).encodeTo(delegate);
      delegate.write(b, 0, len);
    }

    String padded(long orig){
        return String.format("%5s", orig);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
      super.close();
      delegate.close();
    }
  }

  /**
   * Registers {@link ConsoleLineNumberBuildWrapper} as a {@link BuildWrapper}.
   */
  @Extension
  public static final class DescriptorImpl extends BuildWrapperDescriptor {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName() {
      return Messages.DisplayName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isApplicable(AbstractProject<?, ?> item) {
      return true;
    }
  }
}
