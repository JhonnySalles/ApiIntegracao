package br.com.fenix.apiintegracao.repository

/*@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension::class)
class EstatisticaRepositoryTest(@Mock var repository: EstatisticaJaponesRepository, assembler: PagedResourcesAssembler<EstatisticaDto>) {
    lateinit var input: MockEstatistica

    @InjectMocks
    var service = object : ServiceJpaBase<UUID?, EstatisticaJapones, EstatisticaDto, EstatisticaJaponesController>(repository, assembler, EstatisticaJapones::class.java, EstatisticaDto::class.java, EstatisticaJaponesController::class.java) {}

    @BeforeEach
    @Throws(Exception::class)
    fun setUpMocks() {
        input = MockEstatistica()
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testFindById() {
        val id = UUID.fromString("1")
        val entity = input.mockEntity(id)
        Mockito.`when`(repository.findById(id)).thenReturn(Optional.of(entity))
        val result: EstatisticaDto = service[id]
        input.assertsService(result)
    }

    @Test
    fun testCreate() {
        val id = UUID.fromString("1")
        val entity = input.mockEntity(id)
        val persisted = entity
        val dto = input.mockDto(id)
        Mockito.`when`(repository.save(entity)).thenReturn(persisted)
        input.assertsService(service.create(dto))
    }

    @Test
    fun testCreateWithNullObject() {
        val exception: Exception = Assertions.assertThrows(RequiredObjectIsNullException::class.java) {
            service.create(null)
        }
        val expectedMessage = "Its required inform a object"
        val actualMessage = exception.message
        Assertions.assertTrue(actualMessage!!.contains(expectedMessage))
    }

    @Test
    fun testUpdate() {
        val id = UUID.fromString("1")
        val entity = input.mockEntity(id)
        val persisted = entity
        val dto = input.mockDto(id)

        Mockito.`when`(repository.findById(id)).thenReturn(Optional.of(entity))
        Mockito.`when`(repository.save(entity)).thenReturn(persisted)

        input.assertsService(service.update(dto))
    }


    @Test
    fun testUpdateWithNullObject() {
        val exception: Exception = Assertions.assertThrows(RequiredObjectIsNullException::class.java) {
            service.update(null)
        }
        val expectedMessage = "Its required inform a object"
        val actualMessage = exception.message
        Assertions.assertTrue(actualMessage!!.contains(expectedMessage))
    }

    @Test
    fun testDelete() {
        val id = UUID.fromString("1")
        val entity = input.mockEntity(id)
        Mockito.`when`(repository.findById(id)).thenReturn(Optional.of(entity))
        service.delete(id)
    }

    @Test
    fun testFindAll() {
        val list: List<EstatisticaJapones> = input.mockEntityList()
        Mockito.`when`(repository.findAll()).thenReturn(list)
        val dtos = service.getAll()

        Assertions.assertNotNull(dtos)

        input.assertsToDto(list[0], dtos[0])
        input.assertsToDto(list[list.size / 2], dtos[dtos.size / 2])
        input.assertsToDto(list[list.size - 1], dtos[dtos.size - 1])
    }
}*/