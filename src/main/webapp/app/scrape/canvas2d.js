function Canvas2D($canvas)  
{  
    var context = $canvas[0].getContext("2d"),  
        width = $canvas[0].width,  
        height = $canvas[0].height,  
        pageOffset = $canvas.offset();  
  
  
    context.font = "24px Verdana, Geneva, sans-serif";  
    context.textBaseline = "top";  
  
  
    /** 
     * ���ƾ��� 
     * @param start 
     * @param end 
     * @param isFill 
     */  
    this.drawRect = function (start, end, isFill)  
    {  
        var w = end.x - start.x , h = end.y - start.y;  
        if (isFill)  
        {  
            context.fillRect(start.x, start.y, w, h);  
        }  
        else  
        {  
            context.strokeRect(start.x, start.y, w, h);  
        }  
    };  
  
    /** 
     * ������д���ı����õ����ı���canvas����д������λ�õ����Ͻ����� 
     * @param text 
     * @returns {{x: number, y: number}} 
     */  
    this.caculateTextCenterPos = function (text)  
    {  
        var metrics = context.measureText(text);  
        console.log(metrics);  
//        context.font = fontSize + "px Verdana, Geneva, sans-serif";  
        var textWidth = metrics.width;  
        var textHeight = parseInt(context.font);  
  
        return {  
            x: width / 2 - textWidth / 2,  
            y: height / 2 - textHeight / 2  
        };  
    }  
    this.width = function ()  
    {  
        return width;  
    }  
    this.height = function ()  
    {  
        return height;  
    }  
    this.resetOffset = function ()  
    {  
        pageOffset = $canvas.offset();  
    }  
    /** 
     * ����Ļ��С�����仯�����¼���offset 
     */  
    $(window).resize(function ()  
    {  
        pageOffset = $canvas.offset();  
    });  
  
    /** 
     * ��ҳ���ϵ����ת��Ϊcanvas�е����� 
     * @param pageX 
     * @param pageY 
     * @returns {{x: number, y: number}} 
     */  
    this.getCanvasPoint = function (pageX, pageY)  
    {  
        return{  
            x: pageX - pageOffset.left,  
            y: pageY - pageOffset.top  
        }  
    }  
    /** 
     * ������򣬴��û��������ν�Ϳ�� 
     * @param start 
     * @returns {*} 
     */  
    this.clearRect = function (start)  
    {  
        context.clearRect(start.x, start.y, 10, 10);  
        return this;  
    };  
  
    /** 
     *���ı����Ƶ�canvas���м� 
     * @param text 
     * @param fill 
     */  
    this.drawTextInCenter = function (text, fill)  
    {  
        var point = this.caculateTextCenterPos(text);  
        if (fill)  
        {  
            context.fillText(text, point.x, point.y);  
        }  
        else  
        {  
            context.strokeText(text, point.x, point.y);  
        }  
    };  
    /** 
     * ���û��ʿ�� 
     * @param newWidth 
     * @returns {*} 
     */  
    this.penWidth = function (newWidth)  
    {  
        if (arguments.length)  
        {  
            context.lineWidth = newWidth;  
            return this;  
        }  
        return context.lineWidth;  
    };  
  
    /** 
     * ���û�����ɫ 
     * @param newColor 
     * @returns {*} 
     */  
    this.penColor = function (newColor)  
    {  
        if (arguments.length)  
        {  
            context.strokeStyle = newColor;  
            context.fillStyle = newColor;  
            return this;  
        }  
  
        return context.strokeStyle;  
    };  
  
    /** 
     * ���������С 
     * @param fontSize 
     * @returns {*} 
     */  
    this.fontSize = function (fontSize)  
    {  
        if (arguments.length)  
        {  
            context.font = fontSize + "px Verdana, Geneva, sans-serif";  
  
            return this;  
        }  
  
        return context.fontSize;  
    }  
  
  
}  