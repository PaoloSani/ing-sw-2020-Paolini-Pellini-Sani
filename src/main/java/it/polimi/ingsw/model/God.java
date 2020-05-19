package it.polimi.ingsw.model;

import javafx.scene.image.Image;

public enum God {
    APOLLO("APOLLO","",new Image("/GUIScenes/GodCards/01.png")),
    ARTEMIS("ARTEMIS","",new Image("/GUIScenes/GodCards/02.png")),
    ATHENA("ATHENA","",new Image("/GUIScenes/GodCards/03.png")),
    ATLAS("ATLAS","",new Image("/GUIScenes/GodCards/04.png")),
    CHARON("CHARON","",new Image("/GUIScenes/GodCards/15.png")),
    DEMETER("DEMETER","",new Image("/GUIScenes/GodCards/05.png")),
    HEPHAESTUS("HEPHAESTUS","",new Image("/GUIScenes/GodCards/06.png")),
    HYPNUS("HYPNUS","",new Image("/GUIScenes/GodCards/22.png")),
    MINOTAUR("MINOTAUR","",new Image("/GUIScenes/GodCards/08.png")),
    MORTAL("MORTAL","",new Image("/GUIScenes/GodCards/07.png")),
    PAN("PAN","",new Image("/GUIScenes/GodCards/09.png")),
    POSEIDON("POSEIDON","",new Image("/GUIScenes/GodCards/27.png")),
    PROMETHEUS("PROMETHEUS","",new Image("/GUIScenes/GodCards/10.png")),
    TRITON("TRITON","",new Image("/GUIScenes/GodCards/29.png")),
    ZEUS("ZEUS","",new Image("/GUIScenes/GodCards/30.png"));

    God(String godName, String power, Image image){
        this.godName = godName;
        this.power = power;
        this.image = image;
    }

    @Override
    public String toString(){
        return godName;
    }

    public String getPower(){
        return power;
    }

    public Image getImage(){ return image; }

    private String godName;
    private String power;
    private Image image;

}
