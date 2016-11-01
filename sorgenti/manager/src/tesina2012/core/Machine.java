package tesina2012.core;


/**
 *
 * @author andrea
 */
class Machine {
    private int id;
    private int worker;
    private int order;
    private int pieces;
    private State state;
    
    public Machine(Message msg){
        this.id=msg.getMacId();
        this.worker=msg.getOpId();
        this.order=msg.getComId();
        this.pieces=0;
        this.state=msg.getState();
    }
    public Machine(int id,int worker,int order,int pieces,State state){
        this.id=id;
        this.worker=worker;
        this.order=order;
        this.pieces=pieces;
        this.state=state;
    }
    
    public int getId(){
        return id;
    }
    public int getWorker(){
        return worker;
    }
    public int getOrder(){
        return order;
    }
    public int getPieces(){
        return pieces;
    }
    public State getState(){
        return state;
    }
    
    /*public Date getWorkingTime(){
        return
    }
    public Date getTotalTime(){
        
    }*/
    public void changeState(State snew){
        State sold=this.state;
      
        if(sold==State.WORKING&&snew==State.WAITING)
            this.pieces++;
        this.state=snew;
    }
    
    @Override
    public String toString(){
        String str="Macchina numero "+id+"\n";
        str+="Operaio:\t"+worker+"\n";
        str+="Ordine :\t"+order+"\n";
        str+="Pezzi  :\t"+pieces+"\n";
        str+="Stato  :\t"+state+"\n";
        return str;
    }
    
}

