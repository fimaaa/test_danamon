package com.general.repository.datasource

// class EmployeePagingSource(
//    private val employeeApi: EmployeeApi,
//    private val search: String?
// ) : PagingSource<Int, Employee.Data>() {
//    private val STARTING_PAGE_INDEX = 0
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Employee.Data> {
//        return try {
//            val position = params.key ?: STARTING_PAGE_INDEX
//            val list = mutableListOf<Employee.Data>()
//            if (search.isNullOrEmpty()) {
//                val response = employeeApi.getEmployeesData()
//                list.addAll(response.data)
//            } else {
//                val response = employeeApi.getEmployeeData(search)
//                list.add(response.data)
//            }
//            // Show only 20 data because no pagination
//            if (list.size >= 20) list.subList(20, list.size).clear()
//            LoadResult.Page(
//                data = list,
//                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
// //                nextKey = if (response.isNullOrEmpty()) null else position + 1
//                nextKey = null
//            )
//        } catch (e: Exception) {
//            e.printStackTrace()
//            LoadResult.Error(e)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, Employee.Data>): Int = STARTING_PAGE_INDEX
// }