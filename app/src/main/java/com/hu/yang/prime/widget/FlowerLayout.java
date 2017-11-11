package com.hu.yang.prime.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FlowerLayout extends ViewGroup {
	private static final int maxLine = 100;
	//当前自定义控件中有很多行,自定义控件的高度 = 获取每一行的高度叠加+所有行间距+顶端和底端的内边距
	//定义一个行对象相应集合
	private List<Line> lineList = new ArrayList<Line>();
	private Line line;
	private int usedWidth;
	
	//定义控件间的水平间距
	private int horizontolSpacing = 12;
	//定义控件间的竖直间距
	private int verticalSpacing = 20;
	
	public FlowerLayout(Context context) {
		super(context);
	}

	//定义每一个TextView以及TextView所在行在屏幕中的位置
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		//获取默认第0行的左上角位置

		//第0行左侧位置
		int left = getPaddingLeft();
		//获取第0行顶端位置
		int top = getPaddingTop();
		
		for(int i = 0;i<lineList.size();i++){
			Line line = lineList.get(i);
			line.layout(left, top);
			top +=line.lineHeight+verticalSpacing;
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//获取定义宽的模式
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		//获取定义可用的宽度的大小值
		int widthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
		
		//高度的模式
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		//获取可用的高度的大小值
		int heightSize = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
		
		//获取自定义控件中放置的方格,每一个方格都需要做宽高的处理
		
		//获取自定义控件内部方格的总
		int childCount = getChildCount();
		//集合清空,状态归位处理
		restoreLine();
		
		for(int i=0;i<childCount;i++){
			View childView = getChildAt(i);
			//修改childView的宽高值,当前childView的宽高不能超过自定义控件的宽高
			
			//定义内部控件的宽度以及高度的模式,不能超过夫控件
			
			//夫控件的模式为精确(720px),子控件的模式就为至多(不能超过720px),
			
			//夫控件的模式为至多,子节点的模式也为至多,
			//夫控件的模式为未定义,子控件模式也为未定义
			
			int childViewWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, 
					widthMode == MeasureSpec.EXACTLY?MeasureSpec.AT_MOST:widthMode);
			
			int childViewHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, 
					heightMode == MeasureSpec.EXACTLY?MeasureSpec.AT_MOST:heightMode);
			//让子控件,安装自己定义的规则设置宽高值
			childView.measure(childViewWidthMeasureSpec, childViewHeightMeasureSpec);
			
			//获取子控件的宽度值大小,如果小于当前行的可用宽度,则可以添加,
			//如果等于或者大于当前行的宽度,依然添加此view到行对象中,但是需呀换行放置后续的view
			int childWidth = childView.getMeasuredWidth();
			usedWidth += childWidth;
			if(usedWidth<widthSize){
				//能够放入,放置到行对象中去
				line.addView(childView);
				//定义一个行中控件的水平间距
				usedWidth += horizontolSpacing;
				if(usedWidth>widthSize){
					//对应图片中的第二种情况,换行
					if(!newLine()){
						break;
					}
				}
			}else{
				//1,usedWidth == widthsize,先放置此view对象,然后换行,对应图片中第3中情况
	
				//如果当前行没有别的方格,则可以将宽度和行宽度一样大的控件添加进去
				if(line.getViewCount() == 0){
					line.addView(childView);
					if(!newLine()){
						break;
					}
				}else{
					//2,usedWidth> widthSize,换行放置此view对象,对应图片中的第1中情况
					//先换行
					if(!newLine()){
						break;
					}
					//后添加控件
					line.addView(childView);
					usedWidth += childWidth+horizontolSpacing;
				}
			}
		}
		//如果最后一个行对象,没有加到行集合中,则需要添加进去
		
		//行对象不为空,行对象有添加方格,行对象没有包含在行所在集合中
		if(line!=null && line.getViewCount()>0 && !lineList.contains(line)){
			lineList.add(line);
		}
		
		int flowerWidthSize = MeasureSpec.getSize(widthMeasureSpec);
		//自定义控件的高度 = 所有行的高度+所有行间距+自定义控件顶端和底端的内边距
		int flowerHeightSize = 0;
		int count = lineList.size();
		//所有行的高度
		for(int i=0;i<count;i++){
			Line line = lineList.get(i);
			flowerHeightSize += line.lineHeight;
		}
		//添加所有行间距
		flowerHeightSize += (count - 1)*verticalSpacing;
		//添加顶端和底端的内边距
		flowerHeightSize += getPaddingBottom() + getPaddingTop();
		
		//定义控件宽高以及模式的方法
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		//过滤模式,得到一个最终的宽高结果
		setMeasuredDimension(getDefaultSize(flowerWidthSize, widthMeasureSpec),
                getDefaultSize(flowerHeightSize, heightMeasureSpec));
	}
	
	//换行的方法
	private boolean newLine() {
		//1,将以及填充满view的行放置到行集合中去
		lineList.add(line);
		if(lineList.size()<maxLine){
			//2,创建一个新的行对象
			line = new Line();
			//3,新行中已使用的宽度必须赋值成0
			usedWidth = 0;
			return true;
		}
		
		return false;
	}

	private void restoreLine() {
		//清空行集合中所有对象
		lineList.clear();
		//创建一个行对象,准备去添加childView
		line = new Line();
		//生一个记录当前行已经使用掉的宽度值大小的变量
		usedWidth = 0;
	}

	//创建一个行对象,用于放置方格(TextView)
	public class Line{
		//定义当前行中所有方格使用的总宽度值大小所在变量
		private int childViewWidthTotal;
		//定义记录当前行高度的变量
		private int lineHeight;
		//放置方格(TextView)所在的集合
		private List<View> viewList = new ArrayList<View>();
		
		//返回当前行对象中集合总个数的方法
		public int getViewCount(){
			return viewList.size();
		}
		
		//向当前行对象添加一个方格(TextView)的方法
		public void addView(View view){
			viewList.add(view);
			//侧拉TextView相应控件的宽度值大小
			int childWidth = view.getMeasuredWidth();
			//高度值大小
			int childHeight = view.getMeasuredHeight();
			
			//累计当前行中已使用的宽度值大小
			childViewWidthTotal += childWidth;
			
			//定义行高度(当前行最高控件的高度值)
			lineHeight = lineHeight>childHeight?lineHeight:childHeight;
		}
		
		//放置内部子控件的位置,以及留白区域的分配(水平方向留白,竖直方向留白)
		public void layout(int l,int t){
			int left = l;
			int top = t;
			
			//水平留白区域 = 自定义控件可用宽度-行对象内部方格已用的宽度 - 方格和方格总间距
			int validWidth = getMeasuredWidth()-getPaddingLeft() - getPaddingRight();
			
			int surplusWidthTotal = validWidth - childViewWidthTotal - (getViewCount()-1)*horizontolSpacing;
			
			//每一个控件分配到的额外的宽度值大小
			int surplusWidth = surplusWidthTotal/getViewCount();
			if(surplusWidth>0){
				//水平区域有留白行,内部控件的分配规则
				for(int i=0;i<getViewCount();i++){
					View childView = viewList.get(i);
					
					//加上剩余控件后的真实宽度
					int childWidth = childView.getMeasuredWidth()+surplusWidth;
					int childHeight = childView.getMeasuredHeight();
					//安装自己计算出来的高度已经宽度去定义控件
					int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
					int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
					//让控件按自己的规则去绘画,展示
					childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
					//计算当前行中每一个方格对应的水平,竖直位置
					//左侧位置计算规则 left += 前一个控件view的宽度+水平间距
					
					//获取行高度
					int surplusHeightHalf = (lineHeight - childHeight)/2;
					childView.layout(left, top+surplusHeightHalf, left+childWidth,top+surplusHeightHalf+childHeight);
					left += childWidth+horizontolSpacing;
				}
			}else{
				//水平没有留白区域,行对象放置控件的过程
				if(getViewCount() == 1){
					View view = viewList.get(0);
					view.layout(left, top, left+view.getMeasuredWidth(), top+view.getMeasuredHeight());
				}
			}
		}
	}
}
