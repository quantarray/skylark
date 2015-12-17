# Skylark

Skylark is a set of libraries for quantitative and financial computation.

## skylark-learning-neural

**skylark-learning-neural** is a library for training, testing, and using neural networks.

The example below shows how to train and test a feed-forward network using a backpropagation trainer on [MNIST database of handwritten digits](http://yann.lecun.com/exdb/mnist).

```scala
// Load training and test data
val trainingDataProvider = new MnistDataProvider("data/mnist/train-images-idx3-ubyte", "data/mnist/train-labels-idx1-ubyte")

val testDataProvider = new MnistDataProvider("data/mnist/t10k-images-idx3-ubyte", "data/mnist/t10k-labels-idx1-ubyte")

val testSetIsFit = (testDataProvider.read.set, MnistSupervisedDataSample.isFit _)

// Number of nodes in the hidden layer ≈ √ (784 * 10)
val net = FullyConnectedNet(GaussianWeightAssignment, SigmoidActivation, CrossEntropyCost, 784, 88, 10)

// Train the network
val trainer = BackPropagationTrainer(learningRate = 0.005, weightDecay = 0.5)

val numberOfEpochs = 30
val miniBatchSize = 10

// First accuracy is the one of the untrained (random weights) network, second should be ≈ 90%; subsequent accuracies will improve
val trainedNets = trainer.trainAndTest(net, numberOfEpochs, miniBatchSize, trainingDataProvider.read.set, testSetIsFit)

val accuracy = trainer.test(trainedNets.last._1, testSetIsFit)

trainingDataProvider.close()
testDataProvider.close()
```