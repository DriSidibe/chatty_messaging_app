/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chatty.my_classes;

import chatty.service.Chatty_service_interface;
import java.rmi.registry.Registry;

/**
 *
 * @author dsidi
 */
public class GlobalState {
    // Static field
    public static Registry registry;
    public static Chatty_service_interface Chatty_service = null;
}
