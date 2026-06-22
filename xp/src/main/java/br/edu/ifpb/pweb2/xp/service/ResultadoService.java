@Service
public class ResultadoService {

    @Autowired
    private ResultadoRepository repository;
    
    @Autowired
    private ParticipanteService participanteService;

    @Transactional
    public Resultado salvar(Participante participante, Corrida corrida, BigDecimal pontuacao) {
        if (participante == null || participante.getNome() == null) {
            throw new ValidationException("Dados do participante inválidos");
        }
        
        Participante participantePersistido = participanteService.buscarOuCriar(participante.getNome());
        
        Resultado resultado = new Resultado();
        resultado.setParticipante(participantePersistido);
        resultado.setCorrida(corrida);
        resultado.setPontuacao(pontuacao);
        resultado.setDataHora(LocalDateTime.now());
        return repository.save(resultado);
    }

    public Page<Resultado> rankingGeral(Pageable pageable) {
        return repository.findAllByOrderByPontuacaoDescDataHoraDesc(pageable);
    }

    public Page<Resultado> rankingPorCorrida(Long corridaId, Pageable pageable) {
        return repository.findRankingPorCorrida(corridaId, pageable);
    }
}