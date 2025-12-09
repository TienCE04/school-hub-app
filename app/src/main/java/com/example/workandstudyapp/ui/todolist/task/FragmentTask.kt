package com.example.workandstudyapp.ui.todolist.task

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workandstudyapp.R
import com.example.workandstudyapp.data.local.room.AppDatabase
import com.example.workandstudyapp.data.local.room.dao.TaskDao
import com.example.workandstudyapp.data.local.room.entity.TaskEntity
import com.example.workandstudyapp.data.repository.todo.TaskRepository
import com.example.workandstudyapp.databinding.DialogDetailTaskBinding
import com.example.workandstudyapp.databinding.FragmentTaskBinding
import com.example.workandstudyapp.ui.todolist.TodoViewModel
import com.example.workandstudyapp.ui.todolist.data.NumberTaskInDay
import com.example.workandstudyapp.ui.todolist.task.adapter.DayAdapter
import com.example.workandstudyapp.ui.todolist.task.adapter.ItemDayDecoration
import com.example.workandstudyapp.ui.todolist.task.adapter.OnClickInFragmentTask
import com.example.workandstudyapp.ui.todolist.task.adapter.OnClickItemTask
import com.example.workandstudyapp.ui.todolist.task.adapter.TaskAdapter
import com.example.workandstudyapp.ui.todolist.viewmodel.TaskFactory
import com.example.workandstudyapp.ui.todolist.viewmodel.TaskViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

class FragmentTask: Fragment(), View.OnClickListener, OnClickInFragmentTask, OnClickItemTask {

    private lateinit var _binding: FragmentTaskBinding
    private val binding get() = _binding
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskRcv: RecyclerView
    private lateinit var taskRepo: TaskRepository
    private lateinit var taskDao: TaskDao
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var selectedDate: LocalDate
    private val listDayInWeek=mutableListOf<NumberTaskInDay>()
    private val todoViewModel: TodoViewModel by viewModels({requireParentFragment()})
    private var startDate=""
    private var endDate=""
    //
    private lateinit var dayAdapter: DayAdapter
    private lateinit var dayRcv: RecyclerView
    private lateinit var dialogTask: Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding= FragmentTaskBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        selectedDate= LocalDate.now()
        taskDao= AppDatabase.getDatabase(requireContext()).taskDao()
        initListener()
        initViewModel()
        initAdapter()
        initObserve()
    }
    private fun initObserve(){
        taskViewModel.checkTaskIn1Week.observe(viewLifecycleOwner){list->
            val newList = listDayInWeek.map { it.copy() }
            Log.d("DEBUG_observe",list.toString())
            val listDayInWeekTmp=mutableListOf<NumberTaskInDay>()
            if(list.isNotEmpty()){
                for(i in 0..<listDayInWeek.size){
                    val found = list.find { it.createAt == listDayInWeek[i].createAt }
                    if(found!=null){
                        listDayInWeekTmp.add(found)
                    }
                    else{
                        listDayInWeekTmp.add(listDayInWeek[i])
                    }
                }
                dayAdapter.submitList(listDayInWeekTmp)
            }
            else{
                dayAdapter.submitList(newList)
            }
        }

        //bắt đầu khi start và tự hủy khi stop
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    taskViewModel.listTaskInDay.collect {list->
                        if(list.isEmpty()){
                            binding.tvNumberTask.text=requireContext().getString(R.string.no_task)
                        }
                        else{
                            binding.tvNumberTask.text="Bạn có ${list.size} nhiệm vụ hôm nay"
                            taskAdapter.submitList(list)
                        }
                    }
                }
                launch {
                    taskViewModel.isState.collect { isState->
                        if(isState.isNotEmpty()){
                            Toast.makeText(requireContext(),isState, Toast.LENGTH_SHORT).show()
                            taskViewModel.resetState()
                        }
                    }
                }

            }
        }

        todoViewModel.selectedDay.observe(viewLifecycleOwner){value->
            binding.tvDate.text=getDateReal()
        }
    }

    private fun initViewModel(){
        taskRepo= TaskRepository(taskDao)
        taskViewModel= ViewModelProvider(requireActivity(), TaskFactory(taskRepo))[TaskViewModel::class.java]

        if(todoViewModel.selectedDay.value== LocalDate.now().toString()){
            taskViewModel.getListTaskInDay(LocalDate.now().toString())
        }
        getListDayInWeek()

    }

    private fun initListener(){
        binding.nextButton.setOnClickListener(this)
        binding.prevButton.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.nextButton->{
                selectedDate=selectedDate.plusWeeks(1)
                getListDayInWeek()
            }
            R.id.prevButton->{
                selectedDate=selectedDate.minusWeeks(1)
                getListDayInWeek()
            }
            R.id.huyTask->{
                dialogTask.dismiss()
            }
        }
    }

    private fun initAdapter(){
        dayRcv=binding.rcvScheduleWeek
        dayAdapter= DayAdapter(this,todoViewModel.selectedDay.value)
        dayRcv.itemAnimator = null
        dayRcv.layoutManager= LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL,false)
        val spacing = resources.getDimensionPixelSize(R.dimen.spacing_24dp)
        dayRcv.addItemDecoration(ItemDayDecoration(spacing))
        dayRcv.adapter=dayAdapter

        taskRcv=binding.rcvListTask
        taskAdapter= TaskAdapter(this)
        taskRcv.itemAnimator=null
        taskRcv.layoutManager= LinearLayoutManager(requireContext())
        taskRcv.adapter=taskAdapter
    }


    //ý tưởng dựa vào ngày hiện tại tính danh sách các ngày trong tuần
    //thứ 2 tương ứng với value = 1 đến 7 (chủ nhật)
    private fun getListDayInWeek(){
        listDayInWeek.clear()
        val dayInWeek=selectedDate.dayOfWeek.value
        Log.d("DEBUG_dayInWeek",dayInWeek.toString())
        if(dayInWeek==7){
            //lấy start, end để truy vấn
            startDate=selectedDate.toString()
            endDate=selectedDate.plusDays(6).toString()

            listDayInWeek.add(NumberTaskInDay(selectedDate.toString(),0))
            var todayTmp=selectedDate
            for(i in 1..6){
                todayTmp=todayTmp.plusDays(1)
                listDayInWeek.add(NumberTaskInDay(todayTmp.toString(),0))
            }
        }
        else{
            var todayTmp=selectedDate.minusDays(dayInWeek.toLong())
            listDayInWeek.add(NumberTaskInDay(todayTmp.toString(),0))
            Log.d("DEBUG_dayInWeek",todayTmp.toString())
            Log.d("DEBUG_dayInWeek",listDayInWeek.toString())
            //lấy start, end để truy vấn
            startDate=todayTmp.toString()
            endDate=todayTmp.plusDays(6).toString()

            for(i in 1..6){
                todayTmp=todayTmp.plusDays(1)
                listDayInWeek.add(NumberTaskInDay(todayTmp.toString(),0))
            }
        }
        lifecycleScope.launch {
            taskViewModel.checkTasksIn1Week(startDate,endDate)
        }
    }

    override fun onClickDay(day: String) {
        todoViewModel.clickItemDay(day)
        binding.monthYear.text=getMonthYearReal()
        taskViewModel.getListTaskInDay(day)
        Toast.makeText(requireContext(),"Bạn vừa click ngày: $day", Toast.LENGTH_SHORT).show()
    }

    override fun clickCheckBox(task: TaskEntity) {
        taskViewModel.updateTask(TaskEntity(task.idTask,task.title,task.content,task.timeStart,task.createAt,!task.completed,task.flag))
    }

    override fun clickMarkTask(task: TaskEntity) {
        taskViewModel.updateTask(TaskEntity(task.idTask,task.title,task.content,task.timeStart,task.createAt,task.completed,!task.flag))
    }
    override fun clickDeleteTask(idTask:Int) {
       taskViewModel.deleteTask(idTask)
    }

    override fun clickDetailTask(taskEntity: TaskEntity) {
       initDialogTask(taskEntity.idTask,taskEntity.title,taskEntity.content,taskEntity.timeStart)
    }
    private fun initDialogTask(idTask:Int,title:String,content:String,time:String){
        dialogTask= Dialog(requireContext())
        val view= DialogDetailTaskBinding.inflate(layoutInflater)
        view.nameTask.setText(title)
        view.detailTask.setText(content)
        view.timeWork.setText(time)
        view.luuTask.setOnClickListener{
            val titleNew=view.nameTask.text.toString()
            val contentNew=view.detailTask.text.toString()
            var timeNew=view.timeWork.text.toString()
            if(checkInputAddTask(timeNew,contentNew,timeNew)){
                if(checkTimeTask(timeNew)){
                    timeNew=convertTime(timeNew)
                    taskViewModel.updateDetailTask(idTask,titleNew,contentNew,timeNew)
                    dialogTask.dismiss()
                }
                else{
                    Toast.makeText(requireContext(),"Vui lòng nhập thời gian dạng HH:MM", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                dialogTask.dismiss()
            }
        }
        view.huyTask.setOnClickListener(this)
        view.imgClock.setOnClickListener{

        }
        dialogTask.setContentView(view.root)
        dialogTask.window?.setBackgroundDrawableResource(R.drawable.rounded_dialog)
        dialogTask.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialogTask.show()
    }
    fun getDateReal():String{
        val dateReal= LocalDate.parse(todoViewModel.selectedDay.value)
        val dayOfWeek=dateReal.dayOfWeek
        val dayInMonth=dateReal.dayOfMonth
        return "$dayOfWeek, $dayInMonth"
    }

    fun getMonthYearReal():String{
        val dateReal= LocalDate.parse(todoViewModel.selectedDay.value)
        val month=dateReal.month
        val year=dateReal.year
        return "$month $year"
    }
}
