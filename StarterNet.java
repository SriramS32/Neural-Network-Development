/*
@author Sriram Somasundaram
*/
import java.io.*;
import java.util.*;
//FOR CHANGING INIT SETTINGS - change input, hidden output, cases number in main methods, and the wanted output

public class StarterNet
{
    private double[][] ktrainingInputs;
    private double[][] jnodes;
    private double[][] itargetOutput;
    private double[][] irealOutput;
    private double initError= .2;
    private double[][] iWeights;
    private double[][] hWeights;
    private int numLayers;
    private static int cases;
    private double[][][] deltaweights;
    public static final int Input = 81;
    public static final int Hidden = 30;
    public static final int Output = 2;
    public static final double errorMax = .0000001;
    public static final int baseError = 20;
    public static final int maxCycles = 100000;	
    public static final double lowerB = -1.0;
    public static final double upperB = 1.0;
    public static final double learningRate = 0.95;

    public static void main(String[] args) throws IOException{
        Scanner in = new Scanner(System.in);
        File file = new File("StarterWeights.txt");
        if (!file.exists()) file.createNewFile();
        Scanner scan = new Scanner(file);
        Scanner scanIn = new Scanner(new File("StarterInput.txt"));
        System.out.println("Type train or run");
        String t = in.next();
        double[][] iWeights1 = new double[Hidden][Input];
        double[][] hWeights1 = new double[Output][Hidden];
        if(t.equals("train"))
        {
            BufferedWriter bout = new BufferedWriter(new FileWriter(file));
            StarterNet autobot = new StarterNet(iWeights1, hWeights1);
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
            
            
            //getting in all the inputs that will be propogated to check output
            cases=4;
            StarterNet autobot = new StarterNet(iWeights1,hWeights1);
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
            
            autobot.propagateNet(cases); //with 4 cases
            double[][] irealOutput1 = autobot.getOutput();
            for (int n =0;n<cases;n++) {
                for (int i=0;i<Output;i++) {
                    System.out.println("Case " + n + " Output " + i + ": " + irealOutput1[n][i]);
                }
            }
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
        itargetOutput = new double[][] {{0.0, 1.0},{1.0,0.0}};
    }
    
    public double[][] getOutput()
    {
        return irealOutput;
    }
    public void setInput(double[][] a)
    {
        ktrainingInputs = a;
    }
    
    public double inverse(double a)
    {
        return -Math.log(Math.pow(a,-1)-1);
    }
    public StarterNet(double[][] iWeights1, double[][] hWeights1)
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
        //deltaweights = new double[weights.length][][];
        
        //deltaweights[i]=new double[weights[i].length][];
        for(int j=0;j<Hidden;j++)
        {
            //deltaweights[i][j]=new double[weights[i][j].length];
            for(int k=0;k<Input;k++)
            {
                //deltaweights[i][j][k]= 0.0;
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
    
    public double[][] getIWeights()
    {
        return iWeights;
    }
    
    public double[][] getHWeights()
    {
        return hWeights;
    }
    
    public double[][] copy2D(double old[][])
    {
        double[][] new1 = old;
        //May have to do it old style
        return new1;
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
        /*
        So lets just say uh this calculates like the hidden layer,
        you are given a bunch of inputs (50 by 50 pic, 2500 inputs) and then you have
        weights(where layer 0 is the "first output") Iterate over the inputs first and then the hidden nodes.
        */
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
}