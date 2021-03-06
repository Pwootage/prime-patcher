'use strict';

function dedupe(json) {
    let replacementFiles = new Set();
    json.patches.filter(v => v.type == 'ReplaceFile').map(v => v.filename).forEach(v => replacementFiles.add(v))

    console.log(`${replacementFiles.size} unique files`)

    json.patches = json.patches.filter(v => v.type != 'ReplaceFile')

    replacementFiles.forEach(file => {
        json.patches.push({
            "type": "ReplaceFile",
            "replacementFile": 'files/' + file,
            "filename": file,
            "description": "Replace " + file
        });
    });

    console.log(JSON.stringify(json));
}

let theJson = {
    "description": "Auto-generated patchfile",
    "patches": [
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "74db3935.STRG",
            "filename": "74db3935.STRG",
            "description": "Replace 74db3935.STRG"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "PakfileList",
            "diff": "@@ -1021,32 +1021,200 @@\n R%0A57fb7bf0.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2.CMDL%0A98\n@@ -1441,32 +1441,158 @@\n R%0A9db6156c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930.CMDL%0A3c\n@@ -1673,32 +1673,144 @@\n 7f.TXTR%0A18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A776c1a96.C\n@@ -2057,32 +2057,144 @@\n R%0A287bd568.TXTR%0A\n+0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809.TXTR%0A\n 2782648e.CMDL%0A4c\n@@ -20187,32 +20187,200 @@\n R%0A57fb7bf0.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2.CMDL%0A98\n@@ -20607,32 +20607,158 @@\n R%0A9db6156c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930.CMDL%0A3c\n@@ -20839,32 +20839,144 @@\n 7f.TXTR%0A18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A776c1a96.C\n@@ -21223,32 +21223,144 @@\n R%0A287bd568.TXTR%0A\n+0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809.TXTR%0A\n 2782648e.CMDL%0A4c\n@@ -36455,32 +36455,200 @@\n R%0A57fb7bf0.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2.CMDL%0A98\n@@ -36875,32 +36875,158 @@\n R%0A9db6156c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930.CMDL%0A3c\n@@ -37107,32 +37107,144 @@\n 7f.TXTR%0A18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A776c1a96.C\n@@ -37491,32 +37491,144 @@\n R%0A287bd568.TXTR%0A\n+0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809.TXTR%0A\n 2782648e.CMDL%0A4c\n@@ -72659,32 +72659,200 @@\n R%0A57fb7bf0.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2.CMDL%0A98\n@@ -73079,32 +73079,158 @@\n R%0A9db6156c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930.CMDL%0A3c\n@@ -73311,32 +73311,144 @@\n 7f.TXTR%0A18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A776c1a96.C\n@@ -73695,32 +73695,144 @@\n R%0A287bd568.TXTR%0A\n+0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809.TXTR%0A\n 2782648e.CMDL%0A4c\n@@ -93253,32 +93253,200 @@\n R%0A57fb7bf0.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2.CMDL%0A98\n@@ -93743,32 +93743,158 @@\n R%0A9db6156c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930.CMDL%0A3c\n@@ -93975,32 +93975,144 @@\n 7f.TXTR%0A18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A776c1a96.C\n@@ -94359,32 +94359,144 @@\n R%0A287bd568.TXTR%0A\n+0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809.TXTR%0A\n 2782648e.CMDL%0A4c\n@@ -120889,32 +120889,200 @@\n R%0A57fb7bf0.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2.CMDL%0A98\n@@ -121309,32 +121309,158 @@\n R%0A9db6156c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930.CMDL%0A3c\n@@ -121541,32 +121541,144 @@\n 7f.TXTR%0A18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A776c1a96.C\n@@ -121925,32 +121925,144 @@\n R%0A287bd568.TXTR%0A\n+0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809.TXTR%0A\n 2782648e.CMDL%0A4c\n@@ -137605,32 +137605,200 @@\n R%0A57fb7bf0.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2.CMDL%0A98\n@@ -138221,32 +138221,158 @@\n R%0A9db6156c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930.CMDL%0A3c\n@@ -138453,32 +138453,144 @@\n 7f.TXTR%0A18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A776c1a96.C\n@@ -138837,32 +138837,144 @@\n R%0A287bd568.TXTR%0A\n+0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809.TXTR%0A\n 2782648e.CMDL%0A4c\n@@ -154153,32 +154153,200 @@\n R%0A57fb7bf0.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2.CMDL%0A98\n@@ -154573,32 +154573,158 @@\n R%0A9db6156c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930.CMDL%0A3c\n@@ -154805,32 +154805,144 @@\n 7f.TXTR%0A18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A776c1a96.C\n@@ -155189,32 +155189,144 @@\n R%0A287bd568.TXTR%0A\n+0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809.TXTR%0A\n 2782648e.CMDL%0A4c\n@@ -175419,32 +175419,200 @@\n R%0A57fb7bf0.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2.CMDL%0A98\n@@ -175839,32 +175839,158 @@\n R%0A9db6156c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930.CMDL%0A3c\n@@ -176071,32 +176071,144 @@\n 7f.TXTR%0A18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A776c1a96.C\n@@ -176455,32 +176455,144 @@\n R%0A287bd568.TXTR%0A\n+0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809.TXTR%0A\n 2782648e.CMDL%0A4c\n@@ -181761,32 +181761,200 @@\n R%0A57fb7bf0.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2.CMDL%0A98\n@@ -182251,32 +182251,158 @@\n R%0A9db6156c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930.CMDL%0A3c\n@@ -182483,32 +182483,144 @@\n 7f.TXTR%0A18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A776c1a96.C\n@@ -182867,32 +182867,144 @@\n R%0A287bd568.TXTR%0A\n+0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809.TXTR%0A\n 2782648e.CMDL%0A4c\n@@ -195485,24 +195485,192 @@\n fb7bf0.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2.CMD\n@@ -195905,24 +195905,150 @@\n b6156c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930.CMD\n@@ -196137,24 +196137,136 @@\n XTR%0A18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A776c1a\n@@ -196515,24 +196515,136 @@\n XTR%0A287bd568\n+.TXTR%0A0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809\n .TXTR%0A278264\n",
            "filename": "Metroid2.pak",
            "description": "Modify Metroid2.pak's file list"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "6cb4cd9a.STRG",
            "filename": "6cb4cd9a.STRG",
            "description": "Replace 6cb4cd9a.STRG"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "PakfileList",
            "diff": "@@ -895,32 +895,200 @@\n R%0A57fb7bf0.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2.CMDL%0A98\n@@ -1315,32 +1315,158 @@\n R%0A9db6156c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930.CMDL%0A3c\n@@ -1547,32 +1547,144 @@\n 7f.TXTR%0A18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A776c1a96.C\n@@ -1931,32 +1931,144 @@\n R%0A287bd568.TXTR%0A\n+0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809.TXTR%0A\n 2782648e.CMDL%0A4c\n@@ -11423,32 +11423,200 @@\n R%0A57fb7bf0.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2.CMDL%0A98\n@@ -11843,32 +11843,158 @@\n R%0A9db6156c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930.CMDL%0A3c\n@@ -12075,32 +12075,144 @@\n 7f.TXTR%0A18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A776c1a96.C\n@@ -12459,32 +12459,144 @@\n R%0A287bd568.TXTR%0A\n+0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809.TXTR%0A\n 2782648e.CMDL%0A4c\n@@ -75291,32 +75291,200 @@\n R%0A57fb7bf0.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2.CMDL%0A98\n@@ -75711,32 +75711,158 @@\n R%0A9db6156c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930.CMDL%0A3c\n@@ -75943,32 +75943,144 @@\n 7f.TXTR%0A18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A776c1a96.C\n@@ -76327,32 +76327,144 @@\n R%0A287bd568.TXTR%0A\n+0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809.TXTR%0A\n 2782648e.CMDL%0A4c\n@@ -101807,32 +101807,200 @@\n R%0A57fb7bf0.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2.CMDL%0A98\n@@ -102297,32 +102297,158 @@\n R%0A9db6156c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930.CMDL%0A3c\n@@ -102529,32 +102529,144 @@\n 7f.TXTR%0A18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A776c1a96.C\n@@ -102913,32 +102913,144 @@\n R%0A287bd568.TXTR%0A\n+0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809.TXTR%0A\n 2782648e.CMDL%0A4c\n@@ -111159,32 +111159,200 @@\n R%0A57fb7bf0.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2.CMDL%0A98\n@@ -111621,32 +111621,158 @@\n R%0A9db6156c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930.CMDL%0A3c\n@@ -111853,32 +111853,144 @@\n 7f.TXTR%0A18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A776c1a96.C\n@@ -112237,32 +112237,144 @@\n R%0A287bd568.TXTR%0A\n+0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809.TXTR%0A\n 2782648e.CMDL%0A4c\n@@ -136555,32 +136555,200 @@\n R%0A57fb7bf0.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2.CMDL%0A98\n@@ -137045,32 +137045,158 @@\n R%0A9db6156c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930.CMDL%0A3c\n@@ -137277,32 +137277,144 @@\n 7f.TXTR%0A18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A776c1a96.C\n@@ -137661,32 +137661,144 @@\n R%0A287bd568.TXTR%0A\n+0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809.TXTR%0A\n 2782648e.CMDL%0A4c\n@@ -173641,32 +173641,200 @@\n R%0A57fb7bf0.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2.CMDL%0A98\n@@ -174117,32 +174117,158 @@\n R%0A9db6156c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930.CMDL%0A3c\n@@ -174349,32 +174349,144 @@\n 7f.TXTR%0A18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A776c1a96.C\n@@ -174733,32 +174733,144 @@\n R%0A287bd568.TXTR%0A\n+0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809.TXTR%0A\n 2782648e.CMDL%0A4c\n@@ -194109,32 +194109,200 @@\n R%0A57fb7bf0.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2.CMDL%0A98\n@@ -194557,32 +194557,158 @@\n R%0A9db6156c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930.CMDL%0A3c\n@@ -194789,32 +194789,144 @@\n 7f.TXTR%0A18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A776c1a96.C\n@@ -195173,32 +195173,144 @@\n R%0A287bd568.TXTR%0A\n+0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809.TXTR%0A\n 2782648e.CMDL%0A4c\n@@ -217181,32 +217181,200 @@\n R%0A57fb7bf0.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2.CMDL%0A98\n@@ -217601,32 +217601,158 @@\n R%0A9db6156c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930.CMDL%0A3c\n@@ -217833,32 +217833,144 @@\n 7f.TXTR%0A18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A776c1a96.C\n@@ -218217,32 +218217,144 @@\n R%0A287bd568.TXTR%0A\n+0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809.TXTR%0A\n 2782648e.CMDL%0A4c\n@@ -227321,24 +227321,192 @@\n fb7bf0.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2.CMD\n@@ -227895,24 +227895,150 @@\n b6156c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930.CMD\n@@ -228127,24 +228127,136 @@\n XTR%0A18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A776c1a\n@@ -228505,24 +228505,136 @@\n XTR%0A287bd568\n+.TXTR%0A0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809\n .TXTR%0A278264\n",
            "filename": "Metroid3.pak",
            "description": "Modify Metroid3.pak's file list"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "PakfileList",
            "diff": "@@ -1053,24 +1053,192 @@\n fb7bf0.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2.CMD\n@@ -1469,32 +1469,158 @@\n R%0A9db6156c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930.CMDL%0A3c\n@@ -1701,32 +1701,144 @@\n 7f.TXTR%0A18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A776c1a96.C\n@@ -2079,32 +2079,144 @@\n 2c.TXTR%0A287bd568\n+.TXTR%0A0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809\n .TXTR%0A2782648e.C\n@@ -53861,24 +53861,192 @@\n e5b069.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2.CMD\n@@ -54337,24 +54337,150 @@\n b6156c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930.CMD\n@@ -54569,24 +54569,136 @@\n XTR%0A18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A776c1a\n@@ -54947,24 +54947,136 @@\n XTR%0A287bd568\n+.TXTR%0A0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809\n .TXTR%0A278264\n",
            "filename": "Metroid6.pak",
            "description": "Modify Metroid6.pak's file list"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "PakfileList",
            "diff": "@@ -3507,16 +3507,184 @@\n f0.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2\n@@ -3927,16 +3927,142 @@\n 6c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930\n@@ -4159,16 +4159,128 @@\n 18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A77\n@@ -4537,16 +4537,128 @@\n 287bd568\n+.TXTR%0A0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809\n .TXTR%0A27\n",
            "filename": "Metroid4.pak",
            "description": "Modify Metroid4.pak's file list"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "11beb861.STRG",
            "filename": "11beb861.STRG",
            "description": "Replace 11beb861.STRG"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "PakfileList",
            "diff": "@@ -965,32 +965,200 @@\n R%0A57fb7bf0.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2.CMDL%0A98\n@@ -1385,32 +1385,158 @@\n R%0A9db6156c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930.CMDL%0A3c\n@@ -1617,32 +1617,144 @@\n 7f.TXTR%0A18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A776c1a96.C\n@@ -1995,32 +1995,144 @@\n 2c.TXTR%0A287bd568\n+.TXTR%0A0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809\n .TXTR%0A2782648e.C\n@@ -16547,32 +16547,200 @@\n R%0A57fb7bf0.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2.CMDL%0A98\n@@ -16967,32 +16967,158 @@\n R%0A9db6156c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930.CMDL%0A3c\n@@ -17199,32 +17199,144 @@\n 7f.TXTR%0A18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A776c1a96.C\n@@ -17577,32 +17577,144 @@\n 2c.TXTR%0A287bd568\n+.TXTR%0A0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809\n .TXTR%0A2782648e.C\n@@ -116553,24 +116553,192 @@\n fb7bf0.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2.CMD\n@@ -116973,24 +116973,150 @@\n b6156c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930.CMD\n@@ -117205,24 +117205,136 @@\n XTR%0A18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A776c1a\n@@ -117583,24 +117583,136 @@\n XTR%0A287bd568\n+.TXTR%0A0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809\n .TXTR%0A278264\n",
            "filename": "metroid5.pak",
            "description": "Modify metroid5.pak's file list"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "PakfileList",
            "diff": "@@ -1127,16 +1127,184 @@\n f0.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2\n@@ -1547,16 +1547,142 @@\n 6c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930\n@@ -1779,16 +1779,128 @@\n 18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A77\n@@ -2157,16 +2157,128 @@\n 287bd568\n+.TXTR%0A0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809\n .TXTR%0A27\n",
            "filename": "Metroid7.pak",
            "description": "Modify Metroid7.pak's file list"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "PakfileList",
            "diff": "@@ -4185,32 +4185,200 @@\n R%0A57fb7bf0.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2.CMDL%0A98\n@@ -4605,32 +4605,158 @@\n R%0A9db6156c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930.CMDL%0A3c\n@@ -4837,32 +4837,144 @@\n 7f.TXTR%0A18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A776c1a96.C\n@@ -6293,32 +6293,144 @@\n 2c.TXTR%0A287bd568\n+.TXTR%0A0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809\n .TXTR%0A2782648e.C\n@@ -35675,24 +35675,192 @@\n fb7bf0.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2.CMD\n@@ -36165,24 +36165,150 @@\n b6156c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930.CMD\n@@ -36397,24 +36397,136 @@\n XTR%0A18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A776c1a\n@@ -36775,24 +36775,136 @@\n XTR%0A287bd568\n+.TXTR%0A0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809\n .TXTR%0A278264\n",
            "filename": "Metroid1.pak",
            "description": "Modify Metroid1.pak's file list"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "71eef074.CSKR",
            "filename": "71eef074.CSKR",
            "description": "Replace 71eef074.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1ac15fa2.CMDL",
            "filename": "1ac15fa2.CMDL",
            "description": "Replace 1ac15fa2.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4cadcb58.CSKR",
            "filename": "4cadcb58.CSKR",
            "description": "Replace 4cadcb58.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "2782648e.CMDL",
            "filename": "2782648e.CMDL",
            "description": "Replace 2782648e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1c43b540.CSKR",
            "filename": "1c43b540.CSKR",
            "description": "Replace 1c43b540.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "776c1a96.CMDL",
            "filename": "776c1a96.CMDL",
            "description": "Replace 776c1a96.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3cda66e6.CSKR",
            "filename": "3cda66e6.CSKR",
            "description": "Replace 3cda66e6.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57f5c930.CMDL",
            "filename": "57f5c930.CMDL",
            "description": "Replace 57f5c930.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "98bbd924.CSKR",
            "filename": "98bbd924.CSKR",
            "description": "Replace 98bbd924.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "f39476f2.CMDL",
            "filename": "f39476f2.CMDL",
            "description": "Replace f39476f2.CMDL"
        },
        {
            "type": "PakfileList",
            "diff": "@@ -805,16 +805,184 @@\n f0.TXTR%0A\n+8ff8e517.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A94cc8e0e.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0A2883811b.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0Affd9d212.TXTR%0A\n f39476f2\n@@ -1225,16 +1225,142 @@\n 6c.TXTR%0A\n+3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Aa283e265.TXTR%0Ab29c1225.TXTR%0Ab95fe642.TXTR%0A\n 57f5c930\n@@ -1457,16 +1457,128 @@\n 18443363\n+.TXTR%0A3b843f46.TXTR%0A9fcf0d60.TXTR%0A94cc8e0e.TXTR%0A022045c3.TXTR%0A27805bc6.TXTR%0Aa3bb66a5.TXTR%0Ab29c1225.TXTR%0Ab95fe642\n .TXTR%0A77\n@@ -1841,16 +1841,128 @@\n 68.TXTR%0A\n+0b7d7395.TXTR%0A5b3271ca.TXTR%0A94cc8e0e.TXTR%0A8048f092.TXTR%0Aada9c079.TXTR%0Ac2b83ed1.TXTR%0Ac35540de.TXTR%0Acb737809.TXTR%0A\n 2782648e\n@@ -3179,16 +3179,296 @@\n 9ceb8e17\n+.TXTR%0A2b31d007.TXTR%0A04e95e09.TXTR%0A6bdccd4d.TXTR%0A7abc9a97.TXTR%0A9b0fee43.TXTR%0A39d1d7be.TXTR%0A44d79401.TXTR%0A85c5f51c.TXTR%0A318b1b21.TXTR%0A452ee0d3.TXTR%0A900cfc6e.TXTR%0A3004dd7e.TXTR%0A1293805b.TXTR%0A12310556.TXTR%0Aa5fd9c28.TXTR%0Ab5e0506f.TXTR%0Ac8e613d0.TXTR%0Ac0352c51.TXTR%0Acb5a4377.TXTR%0Ae63dc9d0\n .TXTR%0A1a\n",
            "filename": "Metroid8.pak",
            "description": "Modify Metroid8.pak's file list"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "e231af2b.CSKR",
            "filename": "e231af2b.CSKR",
            "description": "Replace e231af2b.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "891e00fd.CMDL",
            "filename": "891e00fd.CMDL",
            "description": "Replace 891e00fd.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "8f66289c.CSKR",
            "filename": "8f66289c.CSKR",
            "description": "Replace 8f66289c.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "e449874a.CMDL",
            "filename": "e449874a.CMDL",
            "description": "Replace e449874a.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "3c673505.CSKR",
            "filename": "3c673505.CSKR",
            "description": "Replace 3c673505.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "57489ad3.CMDL",
            "filename": "57489ad3.CMDL",
            "description": "Replace 57489ad3.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "21352005.CSKR",
            "filename": "21352005.CSKR",
            "description": "Replace 21352005.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4a1a8fd3.CMDL",
            "filename": "4a1a8fd3.CMDL",
            "description": "Replace 4a1a8fd3.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "c5571168.CSKR",
            "filename": "c5571168.CSKR",
            "description": "Replace c5571168.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "ae78bebe.CMDL",
            "filename": "ae78bebe.CMDL",
            "description": "Replace ae78bebe.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "491cfc28.CSKR",
            "filename": "491cfc28.CSKR",
            "description": "Replace 491cfc28.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "223353fe.CMDL",
            "filename": "223353fe.CMDL",
            "description": "Replace 223353fe.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "720d40aa.CSKR",
            "filename": "720d40aa.CSKR",
            "description": "Replace 720d40aa.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "1922ef7c.CMDL",
            "filename": "1922ef7c.CMDL",
            "description": "Replace 1922ef7c.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "257861de.CSKR",
            "filename": "257861de.CSKR",
            "description": "Replace 257861de.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "4e57ce08.CMDL",
            "filename": "4e57ce08.CMDL",
            "description": "Replace 4e57ce08.CMDL"
        },
        {
            "type": "PakfileList",
            "diff": "@@ -49,16 +49,100 @@\n 1d.TXTR%0A\n+2f566cec.TXTR%0A33ce9ee5.TXTR%0A52e13615.TXTR%0A719f387e.TXTR%0A9255bd83.TXTR%0Ad0041b18.TXTR%0A\n 4e57ce08\n@@ -553,16 +553,58 @@\n 44.TXTR%0A\n+091a32d6.TXTR%0A0662a1c7.TXTR%0Ac6a37961.TXTR%0A\n 1922ef7c\n@@ -805,16 +805,100 @@\n 7c.TXTR%0A\n+2f566cec.TXTR%0A8cf8043c.TXTR%0A59c822a0.TXTR%0A719f387e.TXTR%0Ad0041b18.TXTR%0Af85301c6.TXTR%0A\n 223353fe\n@@ -1043,16 +1043,72 @@\n cc.TXTR%0A\n+2f566cec.TXTR%0A8cf8043c.TXTR%0A59c822a0.TXTR%0Af85301c6.TXTR%0A\n ae78bebe\n@@ -1603,16 +1603,58 @@\n 66.TXTR%0A\n+1e5621f5.TXTR%0A9f51a37e.TXTR%0A23b19b14.TXTR%0A\n 4a1a8fd3\n@@ -1785,16 +1785,72 @@\n 9f.TXTR%0A\n+7f5781d8.TXTR%0A091acc33.TXTR%0Add1473fc.TXTR%0Aff45e6c4.TXTR%0A\n 57489ad3\n@@ -1911,16 +1911,58 @@\n 56.TXTR%0A\n+7f5781d8.TXTR%0Add1473fc.TXTR%0A07e032b3.TXTR%0A\n e449874a\n@@ -2083,15 +2083,57 @@\n XTR%0A\n-\n 4f67973\n+e.TXTR%0A8a598dd2.TXTR%0A95e79836.TXTR%0Ac35540d\n e.TX\n",
            "filename": "TestAnim.Pak",
            "description": "Modify TestAnim.Pak's file list"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "bdd0a098.CSKR",
            "filename": "bdd0a098.CSKR",
            "description": "Replace bdd0a098.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "d6ff0f4e.CMDL",
            "filename": "d6ff0f4e.CMDL",
            "description": "Replace d6ff0f4e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "6b894385.CSKR",
            "filename": "6b894385.CSKR",
            "description": "Replace 6b894385.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "00a6ec53.CMDL",
            "filename": "00a6ec53.CMDL",
            "description": "Replace 00a6ec53.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "d42a18a8.CSKR",
            "filename": "d42a18a8.CSKR",
            "description": "Replace d42a18a8.CSKR"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "bf05b77e.CMDL",
            "filename": "bf05b77e.CMDL",
            "description": "Replace bf05b77e.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "cbfa3ff6.CMDL",
            "filename": "cbfa3ff6.CMDL",
            "description": "Replace cbfa3ff6.CMDL"
        },
        {
            "type": "ReplaceFile",
            "replacementFile": "a0d59020.CSKR",
            "filename": "a0d59020.CSKR",
            "description": "Replace a0d59020.CSKR"
        },
        {
            "type": "PakfileList",
            "diff": "@@ -371,16 +371,86 @@\n aa.TXTR%0A\n+3b843f46.TXTR%0A80a7cf0d.TXTR%0Ab34ce6a0.TXTR%0Ab6728b45.TXTR%0Ad31b3fb2.TXTR%0A\n cbfa3ff6\n@@ -2877,16 +2877,170 @@\n 50.TXTR%0A\n+9bd4e4b3.TXTR%0A12b6539f.TXTR%0A42dfbebf.TXTR%0A86ff8630.TXTR%0A355e4212.TXTR%0A604d4dc5.TXTR%0A788432da.TXTR%0Ab29c1225.TXTR%0Ac2de6c2a.TXTR%0Ae8af7174.TXTR%0Ae3001044.TXTR%0A\n bf05b77e\n@@ -3101,16 +3101,86 @@\n d4.TXTR%0A\n+3b843f46.TXTR%0A80a7cf0d.TXTR%0Ab34ce6a0.TXTR%0Ab6728b45.TXTR%0Ad31b3fb2.TXTR%0A\n 00a6ec53\n@@ -3235,16 +3235,100 @@\n 32357eec\n+.TXTR%0A0c32c6da.TXTR%0A087d0789.TXTR%0Aa240a5d3.TXTR%0Aaa2d0ccf.TXTR%0Ac9b164a1.TXTR%0Ac35540de\n .TXTR%0Ad6\n",
            "filename": "SamusGun.pak",
            "description": "Modify SamusGun.pak's file list"
        }
    ],
    "author": "Generated by prime-patcher"
};

dedupe(theJson);
