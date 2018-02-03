/**
 * 
 */
package com.luhao.frame;

import javax.swing.JDialog;
import javax.swing.JLabel;

import com.luhao.service.IFiveService;
import com.luhao.service.impl.FiveServiceImpl;
import com.luhao.vo.FiveVO;

/**
 * @author howroad
 * @Date 2018年1月28日
 * @version 1.0
 */
public class UserInfoDelDialog extends JDialog{
	
	private static final long serialVersionUID = 1L;
	private IFiveService ifs=new FiveServiceImpl();
	private FiveVO fv;
	private JLabel nameLab;
	private JLabel duanweiLab;
	private JLabel socoreLab;
	private JLabel winLab;
	private JLabel sumLab;
	private JLabel shenglvLab;
	private JLabel taopaolvLab;
	

	public UserInfoDelDialog(int id) {
		this.fv=ifs.findById(id);
		this.setTitle("玩家信息");
		this.setBounds(550, 400, 300, 250);
		this.setLayout(null);
		this.init();
		
		this.setVisible(true);
	}

	private void init() {
		this.nameLab = new JLabel("昵    称:"+fv.getUserNickName());
		this.nameLab.setBounds(50, 10, 200, 30);
		this.add(this.nameLab);

		this.duanweiLab = new JLabel("段    位:"+fv.getDuanwei());
		this.duanweiLab.setBounds(50, 30, 200, 30);
		this.add(this.duanweiLab);
		
		this.socoreLab = new JLabel("分    数:"+fv.getSocore());
		this.socoreLab.setBounds(50, 50, 200, 30);
		this.add(this.socoreLab);
		
		this.winLab = new JLabel("胜    场:"+fv.getWin());
		this.winLab.setBounds(50, 70, 200, 30);
		this.add(this.winLab);
		
		this.sumLab = new JLabel("总局数:"+fv.getSum());
		this.sumLab.setBounds(50, 90, 200, 30);
		this.add(this.sumLab);
		
		this.shenglvLab = new JLabel("胜    率:"+fv.getShenglv());
		this.shenglvLab.setBounds(50, 110, 200, 30);
		this.add(this.shenglvLab);
		
		this.taopaolvLab = new JLabel("逃跑率:"+fv.getDuanxianlv());
		this.taopaolvLab.setBounds(50, 130, 200, 30);
		this.add(this.taopaolvLab);
		
	}
}
