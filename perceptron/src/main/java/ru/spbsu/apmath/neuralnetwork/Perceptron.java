package ru.spbsu.apmath.neuralnetwork;

import com.spbsu.commons.math.vectors.Mx;
import com.spbsu.commons.math.vectors.Vec;
import com.spbsu.ml.Trans;

import java.io.File;
import java.io.IOException;

import static com.spbsu.commons.math.vectors.MxTools.multiply;
import static ru.spbsu.apmath.neuralnetwork.StringTools.readMx;

/**
 * Created by IntelliJ IDEA.
 * User: Афонин Сергей (hrundelb@yandex.ru)
 * Date: 06.10.2014
 * Time: 22:23
 */
public class Perceptron extends Trans.Stub {

  private final Mx[] weightMxes;
  private final Vec[] outputs;
  private final Vec[] sums;
  private final Function activationFunction;

  public Perceptron(Mx[] mxes, Function activationFunction) {
    this.weightMxes = mxes;
    this.outputs = new Vec[mxes.length + 1];
    this.sums = new Vec[mxes.length];
    for (int i = 0; i < mxes.length; i++) {
      if (i < mxes.length - 1 && mxes[i].rows() != mxes[i + 1].columns()) {
        throw new IllegalArgumentException(
                String.format("Count of rows (%s) of %s matrix isn't equal to count of columns (%s) of %s matrix",
                        mxes[i].rows(), i, mxes[i + 1].columns(), i + 1));
      }
    }
    this.activationFunction = activationFunction;
  }

  @Override
  public int xdim() {
    return weightMxes[0].columns();
  }

  @Override
  public int ydim() {
    return weightMxes[weightMxes.length - 1].rows();
  }

  @Override
  public Vec trans(Vec x) {
    outputs[0] = x;
    for (int i = 0; i < weightMxes.length; i++) {
      sums[i] = multiply(weightMxes[i], outputs[i]);
      outputs[i + 1] = activationFunction.vecValue(sums[i]);
    }
    return outputs[outputs.length - 1];
  }

  public Vec getOutput(int index) {
    return outputs[index + 1];
  }

  public Vec getSum(int index) {
    return sums[index];
  }

  public void save(String pathToFolder) throws IOException {
    for (int i = 0; i < weightMxes.length; i++) {
      File file = new File(String.format("%s/matrix%s.txt", pathToFolder, i));
      StringTools.printMx(weightMxes[i], file);
    }
  }

  public static Perceptron getPerceptronByFiles(Function activationFunction, String... paths) throws IOException {
    Mx[] mxes = new Mx[paths.length];
    for (int i = 0; i < paths.length; i++) {
      mxes[i] = readMx(new File(paths[i]));
    }
    return new Perceptron(mxes, activationFunction);
  }
}