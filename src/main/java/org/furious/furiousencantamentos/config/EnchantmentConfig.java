package org.furious.furiousencantamentos.config;

import org.furious.furiousencantamentos.FuriousEncantamentos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantmentConfig extends Config {
    public EnchantmentConfig(FuriousEncantamentos plugin, String fileName) {
        super(plugin, fileName);
    }

    public List<Map<String,List<Integer>>> getEnchantments() {
        return (List<Map<String,List<Integer>>>) getCustomConfig().get("encantamentos");
    }

    public List<Map<Integer,Map<String,Integer>>> getLevels(){
        return (List<Map<Integer,Map<String,Integer>>>) getCustomConfig().get("levels");
    }

    public Map<String,String> getMensagens(){
        return listToMap((List<Map<String,String>>) getCustomConfig().getList("mensagens"));
    }

    public Integer getBookshelfs(){
        return (Integer) getCustomConfig().get("bookshelfs");
    }

    public <K, V> Map<K, V> listToMap(List<Map<K,V>> list){
        Map<K,V> toReturn = new HashMap<>();
        for(Map<K,V> map : list){
            toReturn.putAll(map);
        };
        return toReturn;
    }
}
