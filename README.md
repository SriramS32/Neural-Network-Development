# Neural-Network-Development

Hello, here I will be displaying the progression of interest in neural networks.
I started with developing a single layer MLP that recognized 81 inputs fed in as a 9 x 9 square of 1's and 0's.
The network progressed to recognizing images, and tripplin the input number to include RGB colors. Then, I came to
the problem of how can I return and reverse the inputs back. I thought I must be able to retrieve the ideal solutions
somehow hidden in the information of the weights. All I would need to do is find the inputs for which the I.W (input.weights)
propagated through the net equals the ideal output.

#Thought Process
Initially, I thought of decomposition techniques and finding how much inputs
could be worth based on the magnitude of weights and magnitude of hidden nodes or output nodes.
- This way did not really work as the inputs could be many sets of magnitude resulting in the same conditions of weights and magnitude of hidden nodes - various solutions
I thought of making an n x n x n matrix with n inputs, hidden nodes, and outputs
- As such I would have a set of linear equations, the problem had to do with the logistic function in the domain and range reversal.
Finally, I accepted the fact that there may not be a super easy way to ideally reverse the inputs back as there could be many solutions
among other problems. I decided to train the inputs.
- I minimized the error function with respect to the inputs and kept the weights constant rather than changing the weights while assuming a constant input. I found that the reverse training converged very quickly.