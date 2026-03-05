package com.example.itassetmanagement.controller;

import com.example.itassetmanagement.model.Asset;
import com.example.itassetmanagement.service.AssetService;  // ← changed
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/assets")
public class AssetController {

    private final AssetService assetService;  // ← changed from AssetRepository

    public AssetController(AssetService assetService) {  // ← changed
        this.assetService = assetService;
    }

    @GetMapping
    public String listAssets(Model model) {
        List<Asset> assets = assetService.getAllAssets();  // ← now via service
        model.addAttribute("assets", assets);
        return "assets/list";
    }
}