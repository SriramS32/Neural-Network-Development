/*
@author Sriram Somasundaram
*/
import java.io.*;
import java.util.*;
//FOR CHANGING INIT SETTINGS - change input, hidden output, cases number in main methods, and the wanted output
public class ReverseNetTrainNode
{
    private double[][] ktrainingInputs;
    private double[][] jnodes;
    private double[][] itargetOutput;
    private double[][] irealOutput;
    //private double[][] psi;
    private double initError= .4;
    private double[][] iWeights;
    private double[][] hWeights;
    private int numLayers;
    private static int cases;
    private double[][][] deltaweights;
    public static final int Input = 81;
    public static final int Hidden = 10;
    public static final int Output = 2;
    public static final double errorMax = .0000001;
    public static final int baseError = 20;
    public static final int maxCycles = 100000; 
    public static final double lowerB = -1.0;
    public static final double middleB = 0.0;
    public static final double upperB = 1.0;
    public static final double learningRate = 0.5;

    public static void main(String[] args) throws IOException{
        //weights[][][]=initializeWeights();
        Scanner in = new Scanner(System.in);
        File file = new File("/users/Sriram/Desktop/2014-15/Neural Nets/weightsFail.txt");
        //File file = new File("/users/Sriram/Desktop/2014-15/Neural Nets/weights4reverseSuccess1.txt");
        if (!file.exists()) file.createNewFile();
        File file2 = new File("/users/Sriram/Desktop/2014-15/Neural Nets/InputReverseFail.txt");
        if (!file2.exists()) file2.createNewFile();
        Scanner scan = new Scanner(file);
        Scanner scanIn = new Scanner(new File("/users/Sriram/Desktop/2014-15/Neural Nets/InputEasy.txt"));
        //Scanner scanIn = new Scanner(new File("/users/Sriram/Desktop/2014-15/Neural Nets/InputReverseSuccess1.txt"));
        System.out.println("Type train or run or reverse");
        String t = in.next();
        double[][] iWeights1 = new double[Hidden][Input];
        double[][] hWeights1 = new double[Output][Hidden];
        if(t.equals("train"))
        {
            BufferedWriter bout = new BufferedWriter(new FileWriter(file));
            ReverseNetTrainNode autobot = new ReverseNetTrainNode(iWeights1, hWeights1);
            autobot.initializeWeights();
            
            //taking in the inputs
            cases=2;
            double[][] ktrainingInputs1 = new double[cases][Input];
            //scans files and takes in the inputs, scans line and then by character.
            for (int n=0;n<cases;n++) {
                for (int k=0;k<Math.sqrt(Input);k++) {
                    String line = scanIn.nextLine();
                    for(int b=0;b<Math.sqrt(Input);b++){
                        ktrainingInputs1[n][k*(int)(Math.sqrt(Input))+b]=Character.getNumericValue(line.charAt(b));
                        //
                        //System.out.println(ktrainingInputs1[n][k*9+b]);
                    }
                }
            }
            autobot.setInput(ktrainingInputs1);
            autobot.initOut(cases);
            autobot.initHid(cases);
            autobot.setOut();
            //setting ideal output and initializing real output
            //itargetOutput[0][k]
            
            autobot.changeWeights();
            iWeights1=autobot.getIWeights();
            hWeights1=autobot.getHWeights();
            for (int j=0;j<Hidden;j++)
            {
                for (int k=0;k<Input;k++)
                {
                     bout.write(iWeights1[j][k] + "\n");
                }
            }
                
            for(int i=0;i<Output;i++)
            {
                for (int j=0;j<Hidden;j++)
                {
                    bout.write(hWeights1[i][j] + "\n");
                }
            }
            bout.close();
            System.out.println("Done");
        }
        else if (t.equals("run"))
        {
            //getting the weights from the optimized weights file
            
            for (int j=0;j<Hidden;j++)
            {
                for (int k=0;k<Input;k++)
                {
                    iWeights1[j][k]=scan.nextDouble();
                }
            }
            
            for (int i=0;i<Output;i++)
            {
                for (int j=0;j<Hidden;j++)
                {
                    hWeights1[i][j]=scan.nextDouble();
                }
            }
            
            
            //getting in all the inputs that will be propagated to check output
            cases=4;
            ReverseNetTrainNode autobot = new ReverseNetTrainNode(iWeights1,hWeights1);
            //need to set cases first, because of constructor
            double[][] ktrainingInputs1 = new double[cases][Input];
            autobot.initOut(cases);
            autobot.initHid(cases);
            for (int n=0;n<cases;n++) {
                for (int k=0;k<Math.sqrt(Input);k++) {
                    String line = scanIn.nextLine();
                    for(int b=0;b<Math.sqrt(Input);b++){
                        ktrainingInputs1[n][k*(int)(Math.sqrt(Input))+b]=Character.getNumericValue(line.charAt(b));
                        //System.out.println(ktrainingInputs1[n][k*9+b]);
                    }
                }
            }
            autobot.setInput(ktrainingInputs1);
            //setting ideal output
            
            autobot.propagateNet(cases); //with 5 cases
            double[][] irealOutput1 = autobot.getOutput();
            for (int n =0;n<cases;n++) {
                for (int i=0;i<Output;i++) {
                    System.out.println("Case " + n + " Output " + i + ": " + irealOutput1[n][i]);
                }
            }
            System.out.println("Done");
        }
        else if(t.equals("reverse"))
        {
            BufferedWriter bout = new BufferedWriter(new FileWriter(file2));

            for (int j=0;j<Hidden;j++)
            {
                for (int k=0;k<Input;k++)
                {
                    iWeights1[j][k]=scan.nextDouble();
                }
            }
            
            for (int i=0;i<Output;i++)
            {
                for (int j=0;j<Hidden;j++)
                {
                    hWeights1[i][j]=scan.nextDouble();
                }
            }
            
            
            
            //
            cases=2;
            ReverseNetTrainNode autobot = new ReverseNetTrainNode(iWeights1,hWeights1);
            
            //taking in the inputs
            //cases=4;
            double[][] ktrainingInputs1 = new double[cases][Input];
            //initialized and then set the input nodes
            autobot.setInput(ktrainingInputs1);
            autobot.setOut();
            //setting ideal output and initializing real output
            autobot.initOut(cases);
            autobot.initHid(cases);
            //initialize output and hidden nodes
            
            autobot.changeNodes();
            
            double[][] ktrainingInputs2 = autobot.getInputs();
            for (int n=0;n<cases;n++) {
                for (int k=0;k<Math.sqrt(Input);k++) {
                    
                    for(int b=0;b<Math.sqrt(Input);b++){
                        //System.out.print((int)ktrainingInputs1[n][k*(int)(Math.sqrt(Input))+b]);
                        System.out.print(Math.round(ktrainingInputs2[n][(k*2)+b]) + " ");
                        //bout.write((int)ktrainingInputs2[n][k*(int)(Math.sqrt(Input))+b]);
                        bout.write(ktrainingInputs2[n][(k*2)+b] + " ");
                    }
                    bout.write("\n");
                    System.out.println();
                }
            }
            bout.close();
            System.out.println("Done");
        }
    }
    
    public void initOut(int c)
    {
        irealOutput = new double[c][Output];
    }
    
    public void initHid(int c)
    {
        jnodes = new double[c][Hidden];
    }
    
    public void setOut()
    {
        itargetOutput = new double[][] {{1.0, 0.0},{0.0, 1.0}};
        //Make sure this has new outputs updated
    }
    
    public double[][] getOutput()
    {
        return irealOutput;
    }
    public void setInput(double[][] a)
    {
        ktrainingInputs = a;
    }
    /*
    public double[] reverse()
    {
        //Traverse backwards through the network, undo the activation function
        //and then divide by the weights, rinse and repeat
        //assume ideal output is 1, and only one output
        //UHOH its more complex, may need n by n equation solver
        /*
        The hidden layer is the sum, same output is a sum not direct feed (one to one)
        will need to do something like switch inputs and weights and then train to get
        output, the resulting weights are input, may need to make "weights (inputs)"
        converge, average or something
        */
        //double[] calcNodes1 =new double[Hidden];
        
            /*
            calcNodes[i]
            for(int j=0,j<weights[i].length;i++)
            {
                for (int k=0;k<weights[i][j].length;k++) {
                    calcNodes[j][k]=inverse[1]/weights[i][j][k];
                }
                //so first you need 1 as output, then hidden to input.
            }
            
            double[] calcNodes2 = new double[Input]
            for (int j=0;j<Hidden;j++) {
                calcNodes1[j]=inverse(1)/weights[1][j][0];
            }
            for (int k=0;k<Input;k++) {
                calc9Nodes2[k]=inverse(calcNodes1[0])/weights[0][0][k];
                //this would give me the weight connection between the
                //first node of the hidden layer and every input
            }
            return calcNodes2;
    }
    */
    public double inverse(double a)
    {
        return -Math.log(Math.pow(a,-1)-1);
    }
    public ReverseNetTrainNode(double[][] iWeights1, double[][] hWeights1)
    {
        iWeights=iWeights1;
        hWeights=hWeights1;
        jnodes = new double[cases][Hidden];
    }
    public double randBound (double lowerB1, double upperB1)
    {
        return Math.random()*(upperB1-lowerB1)+lowerB1;
    }
    public void initializeWeights()
    {
        for(int j=0;j<Hidden;j++)
        {
            
            for(int k=0;k<Input;k++)
            {
                
                iWeights[j][k]=randBound(lowerB,upperB);
                //System.out.println("weights[i][j][k]: " + weights[i][j][k]);
                }
            }
            
        for(int i=0;i<Output;i++)
        {
            for(int j=0;j<Hidden;j++)
            {
                hWeights[i][j]=randBound(lowerB,upperB);
            }
        }
        return;
    }
    
    public void initializeNodes()
    {
        for(int j=0;j<cases;j++)
        {
            for(int k=0;k<Input;k++)
            {
                ktrainingInputs[j][k]=randBound(middleB,upperB);
                //System.out.println("ktrainingInputs[j][k]: " + ktrainingInputs[j][k]);
                }
            }
        /*    
        for(int i=0;i<cases;i++)
        {
            for(int j=0;j<Hidden;j++)
            {
                jnodes[cases][j]=randBound(lowerB,upperB);
                //System.out.println("jnodes[j][k]: " + jnodes[j][k]);
            }
        }
        */
        return;
    }
    
    public double[][] getIWeights()
    {
        return iWeights;
    }
    
    public double[][] getHWeights()
    {
        return hWeights;
    }
    
    public double[][] getInputs()
    {
        return ktrainingInputs;
    }
    
    public double activation(double x)
    {
        return (1/(1+Math.exp(-x)));
    }
    public double derv(double x)
    {
        return x*(1-x);
    }
    public double dervActivation(double x)
    {
        return derv(activation(x));
    }

    public void calculateHidden(double[] input, int n)
    {
            double[] output = new double[Hidden];
            double temp=0; 
            for (int j=0;j<Hidden;j++)
            {
                for(int k=0;k<input.length;k++)
                {
                    temp += input[k]*iWeights[j][k];
                }
                output[j]=activation(temp);
            }
            jnodes[n] = output;
            return;
    }

    public void calculateOutput(double[] input, int n)
    {
        /*
        So lets just say uh this calculates like the hidden layer,
        you are given a bunch of inputs (50 by 50 pic, 2500 inputs) and then you have
        weights(where layer 0 is the "first output") Iterate over the inputs first and then the hidden nodes.
        */
            double[] output = new double[Output];
            for (int i=0;i<Output;i++)
            {
                double temp=0;
                for(int j=0;j<input.length;j++)
                {
                    temp += input[j]*hWeights[i][j];
                }
                output[i]=activation(temp);
            }
            irealOutput[n] = output;
            return;
    }

    public double error()
    {
        /*
        yea, set an error 0 and then sum equals consistent squares of diff between target and calculated,
        uses a temp variable to hold the evaluated inputs. Correct indices.
        */
        double error = 0.0;
        //double[][] calcOutput = new double[Input][];
        for(int n=0;n<cases;n++)
        {
            //double[][] temp=jnodes;
            //calcOutput[k]=temp[Hidden-1];
            for (int i=0;i<Output;i++) {
                error+=Math.pow((itargetOutput[n][i]-irealOutput[n][i]),2);
            }
        }
        //System.out.println(error);
        return (error/2.0);
    }

    public void propagateNet(int a)
    {
        for (int n =0;n<a;n++) {
            calculateHidden(ktrainingInputs[n],n);
            calculateOutput(jnodes[n],n);
        }
        return;
    }
    
  
    public void changeWeights()
    {
        double[][] tempNodes;
        //loop over all the inputs (how many cases or pictures and such)
        double currError =1;
        int currCycles = 0;
        //this does the randomization unless we get weights with errors resulting less than the start max
        while(currError>initError && currCycles<maxCycles)
        {
            initializeWeights();
            propagateNet(cases);
            currError=error();
            currCycles++;
        }
        System.out.println("Initial Error: " + currError);
        currCycles = 0;
        while(currError>errorMax && currCycles<maxCycles)
        {
            propagateNet(cases);
            train();
            currError=error();
            currCycles++;
        }
        System.out.println("Final Error: " + currError);
        return;
    }

    public void train()
    {
        for(int n=0;n<cases;n++)
        {
            //double[] thetai=new double[Output];
            double[] omegai=new double[Output];
            double[] psii = new double[Output];
            double[] bigOmega = new double[Hidden];
            for (int j=0;j<Hidden;j++) {
                for (int i=0;i<Output;i++) {
                    omegai[i]= itargetOutput[n][i]-irealOutput[n][i];
                    psii[i]=omegai[i]*dervActivation(irealOutput[n][i]);
                    hWeights[i][j]+=learningRate*psii[i]*jnodes[n][j];
                    bigOmega[j]+=psii[i]*hWeights[i][j];
                }
                
            }
            for (int k=0;k<Input;k++) {
                for (int j=0;j<Hidden;j++) {
                    iWeights[j][k]+=learningRate*bigOmega[j]*ktrainingInputs[n][k]*dervActivation(jnodes[n][j]);
                }
            }
            
        }
        return;
    }
    
    public void trainNodes()
    {
        for(int n=0;n<cases;n++)
        {
            //double[] thetai=new double[Output];
            double[] omegai=new double[Output];
            double[] psii = new double[Output];
            double[] bigOmega = new double[Hidden];
            for (int j=0;j<Hidden;j++) {
                for (int i=0;i<Output;i++) {
                    omegai[i]= itargetOutput[n][i]-irealOutput[n][i];
                    psii[i]=omegai[i]*dervActivation(irealOutput[n][i]);
                    jnodes[n][j] +=learningRate*psii[i]*hWeights[i][j];
                    //may need to take the hidden calculation out, depending
                    bigOmega[j]+=psii[i]*hWeights[i][j];
                }
                
            }
            for (int k=0;k<Input;k++) {
                for (int j=0;j<Hidden;j++) {
                    ktrainingInputs[n][k]+=learningRate*bigOmega[j]*iWeights[j][k]*dervActivation(jnodes[n][j]);
                }
            }
        }
        return;
    }
    
  
    public void changeNodes()
    //only diff is calling the diff train method, diff initialize method
    {
        //double[][] tempNodes;
        //loop over all the inputs (how many cases or pictures and such)
        double currError =5;
        int currCycles = 0;
        //this does the randomization unless we get weights with errors resulting less than the start max
        while(currError>initError && currCycles<maxCycles)
        {
            initializeNodes();
            propagateNet(cases);
            currError=error();
            currCycles++;
        }
        System.out.println("Initial Error: " + currError);
        currCycles = 0;
        while(currError>errorMax && currCycles<maxCycles)
        {
            propagateNet(cases);
            trainNodes();
            currError=error();
            currCycles++;
        }
        System.out.println("Final Error: " + currError);
        return;
    }
}