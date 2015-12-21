# Neural-Network-Development

Hello, here I will be displaying the progression of interest in neural networks.
The most recent and useful files are in the other project - Reverse Neural Network. The files in this repo are sparsely commented with the important information in the files of the other project

- StarterNet.java: first MLP built that recognized 81 inputs as a 9 x 9 square of 1's and 0's.
- ReverseNetDiffAct.java: MLP that still only recognized the numbers in text file but has a different activation function - didn't work


#Thought Process
I started with developing a single layer MLP that recognized 81 inputs fed in as a 9 x 9 square of 1's and 0's. The network progressed to recognizing images, and trippling the input number to include RGB colors.

I wanted to be able to create images from the neural network and reverse the inputs back. I thought I must be able to retrieve the ideal solutions
somehow hidden in the information of the weights. For ease of explanation, let's consider a network with 1 input, 1 hidden node (in 1 layer), and 1 output. As such, after training the weights between all the nodes are known and an output is specified. The nodes from earlier layers and eventually the input can be calculated by using the inverse of the activation function (which I have created in my initial programs). In sentences:

- output = activation(hidden node * hidden weight)
- hidden node = inverse(output) / hidden weight

The process can be repeated to find the input. However, this method will only work for this simplest case. As there are more hidden nodes and inputs:
- output = activation(hNode1 *hWeight1 + hNode2 *hWeight2)

Although, the weights and output are known, the nodes could have various solutions or in fact, infinite (in the case above, as there are two unknown variables but only one equation). I was thinking of some way to collate various equations in the networks like all the equations of hidden nodes to outputs in order to solve the values for hidden nodes and then inputs. To do so, there would need to be an n x n ... x n network and solving for inputs becomes as simple as solving a matrix with n unknowns and n variables (hopefully none are linearly dependent). This is pretty unreasonable in most applicable neural networks, but I wanted to see if it was theoretically possible. I realized that the domain and range of the activation function is annoying when trying to reverse and results in a lot of imaginary calculations, so I tried to change the activation function (but it didn't work :( )

Sigh. At this point, I thought it may not be possible to find an exact solution, and I arrived at the idea that I could train the inputs just as I trained the weights. I looked at the multivariable calculations performed for the training of the weights. The error was minimized with respect to the inputs while the weights were kept constant, and I coded the math into new train functions. At this point, I was not expecting much, but I actually saw meaningful input reversals!!! 
I tried to make a video of the network drawing an image by training the inputs over epochs, but the network actually trained inputs really fast with a speedy convergence in many different cases.

Old Notes:
Initially, I thought of decomposition techniques and finding how much inputs
could be worth based on the magnitude of weights and magnitude of hidden nodes or output nodes.
- This way did not really work as the inputs could be many sets of magnitude resulting in the same conditions of weights and magnitude of hidden nodes - various solutions
I thought of making an n x n x n matrix with n inputs, hidden nodes, and outputs
- As such I would have a set of linear equations, the problem had to do with the logistic function in the domain and range reversal.
Finally, I accepted the fact that there may not be a super easy way to ideally reverse the inputs back as there could be many solutions
among other problems. I decided to train the inputs.
- I minimized the error function with respect to the inputs and kept the weights constant rather than changing the weights while assuming a constant input. I found that the reverse training converged very quickly.